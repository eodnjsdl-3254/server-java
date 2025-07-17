package kr.hhplus.be.server.ecommerce.service.order.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import kr.hhplus.be.server.ecommerce.controller.coupon.CouponController;
import kr.hhplus.be.server.ecommerce.controller.order.dto.OrderItemRequest;
import kr.hhplus.be.server.ecommerce.domain.CouponType;
import kr.hhplus.be.server.ecommerce.domain.coupon.Coupon;
import kr.hhplus.be.server.ecommerce.domain.coupon.UserCoupon;
import kr.hhplus.be.server.ecommerce.domain.order.Order;
import kr.hhplus.be.server.ecommerce.domain.order.OrderItem;
import kr.hhplus.be.server.ecommerce.domain.order.OrderStatus;
import kr.hhplus.be.server.ecommerce.domain.order.PaymentTransaction;
import kr.hhplus.be.server.ecommerce.domain.product.Product;
import kr.hhplus.be.server.ecommerce.domain.user.UserBalance;
import kr.hhplus.be.server.ecommerce.service.coupon.ICouponService;
import kr.hhplus.be.server.ecommerce.service.order.IOrderService;
import kr.hhplus.be.server.ecommerce.service.order.IPaymentService;
import kr.hhplus.be.server.ecommerce.service.product.IProductService;
import kr.hhplus.be.server.ecommerce.service.product.impl.ProductServiceImpl;
import kr.hhplus.be.server.ecommerce.service.user.IBalanceService;

@Service
public class OrderServiceImpl implements IOrderService {

    private final IProductService productService;
    private final IBalanceService balanceService;
    private final ICouponService couponService;
    private final IPaymentService paymentService;

    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final Map<Long, List<OrderItem>> orderItemsByOrder = new ConcurrentHashMap<>(); // 주문별 아이템
    private final AtomicLong orderIdCounter = new AtomicLong(0L);
    private final AtomicLong orderItemIdCounter = new AtomicLong(0L);

    public OrderServiceImpl(IProductService productService, IBalanceService balanceService, ICouponService couponService, IPaymentService paymentService) {
        this.productService = productService;
        this.balanceService = balanceService;
        this.couponService = couponService;
        this.paymentService = paymentService;
    }

    @Override
    public Order createOrder(Long userId, List<OrderItemRequest> itemRequests, Long userCouponId) {
        if (itemRequests == null || itemRequests.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }

        List<OrderItem> currentOrderItems = new ArrayList<>();
        BigDecimal provisionalTotalAmount = BigDecimal.ZERO;

        // 1. 상품 재고 확인 및 가격 계산
        for (OrderItemRequest req : itemRequests) {
            Product product = productService.getProduct(req.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + req.getProductId()));

            if (product.getStock() < req.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product " + product.getName() + ". Available: " + product.getStock() + ", Requested: " + req.getQuantity());
            }

            // 재고 감소 (동시성 이슈 고려 심화과제) - Mock에서는 단순 감소
            // 실제라면 트랜잭션, 락킹, 분산락 등을 고려해야 함
            if (!productService.decreaseStock(req.getProductId(), req.getQuantity())) {
                throw new IllegalStateException("Failed to decrease stock for product: " + req.getProductId());
            }

            OrderItem orderItem = new OrderItem(
                    orderItemIdCounter.incrementAndGet(),
                    null, // orderId는 나중에 채워짐
                    req.getProductId(),
                    req.getQuantity(),
                    product.getPrice()
            );
            currentOrderItems.add(orderItem);
            provisionalTotalAmount = provisionalTotalAmount.add(orderItem.calculateItemTotal());
        }

        // 2. 쿠폰 적용 (있을 경우)
        BigDecimal finalAmount = provisionalTotalAmount;
        if (userCouponId != null) {
            Optional<UserCoupon> optUserCoupon = couponService.useCoupon(userCouponId);
            if (optUserCoupon.isEmpty()) {
                // 쿠폰 사용 실패 시, 감소시킨 재고를 다시 원복해야 함 (롤백)
                currentOrderItems.forEach(item -> productService.increaseStock(item.getProductId(), item.getQuantity()));
                throw new IllegalArgumentException("Invalid or unusable coupon: " + userCouponId);
            }
            UserCoupon usedUserCoupon = optUserCoupon.get();
            Optional<Coupon> optCoupon = couponService.getCouponById(usedUserCoupon.getCouponId());
            if (optCoupon.isPresent()) {
                Coupon coupon = optCoupon.get();
                if (coupon.getCouponType() == CouponType.PERCENTAGE) {
                    finalAmount = finalAmount.multiply(BigDecimal.ONE.subtract(coupon.getDiscountValue()));
                } else if (coupon.getCouponType() == CouponType.AMOUNT) {
                    finalAmount = finalAmount.subtract(coupon.getDiscountValue());
                    if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
                        finalAmount = BigDecimal.ZERO; // 할인 금액이 상품 총액보다 클 경우 0원 처리
                    }
                }
            }
        }

        Long newOrderId = orderIdCounter.incrementAndGet();
        Order newOrder = new Order(
                newOrderId,
                userId,
                finalAmount,
                LocalDateTime.now(),
                OrderStatus.PENDING, // 초기 상태는 결제 대기
                userCouponId,
                currentOrderItems
        );

        // OrderItem에 orderId 설정
        currentOrderItems.forEach(item -> item.setOrderId(newOrderId));

        orders.put(newOrderId, newOrder);
        orderItemsByOrder.put(newOrderId, currentOrderItems);

        System.out.println("Mock Order created: " + newOrderId + " for user " + userId + ". Total Amount: " + finalAmount);
        return newOrder;
    }

    @Override
    public Order processPayment(Long orderId, String paymentMethod) {
        Order order = orders.get(orderId);
        if (order == null || order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Order not found or not in PENDING state for orderId: " + orderId);
        }

        // 1. 사용자 잔액 확인 및 차감
        UserBalance balance = balanceService.getBalance(order.getUserId());
        if (balance.getAmount().compareTo(order.getTotalAmount()) < 0) {
            // 잔액 부족 시 주문 상태 변경 및 예외 처리
            order.setStatus(OrderStatus.FAILED);
            System.out.println("Mock Payment failed due to insufficient balance for order " + orderId);
            // 재고 원복
            orderItemsByOrder.get(orderId).forEach(item -> productService.increaseStock(item.getProductId(), item.getQuantity()));
            // 사용된 쿠폰 원복
            if (order.getUserCouponId() != null) {
                couponService.cancelCouponUsage(order.getUserCouponId());
            }
            throw new IllegalArgumentException("Insufficient balance for user " + order.getUserId() + ". Required: " + order.getTotalAmount() + ", Available: " + balance.getAmount());
        }

        // 잔액 차감
        balanceService.deductBalance(order.getUserId(), order.getTotalAmount());
        System.out.println("Mock Balance deducted for user " + order.getUserId() + ". Amount: " + order.getTotalAmount());

        // 2. 결제 트랜잭션 생성 및 확인 (Mock)
        PaymentTransaction payment = paymentService.initiatePayment(order.getOrderId(), order.getTotalAmount(), paymentMethod);
        paymentService.confirmPayment(payment.getPaymentId(), "MOCK_APPROVAL_CODE_" + order.getOrderId());

        // 3. 주문 상태 업데이트
        order.setStatus(OrderStatus.PAID);
        order.setOrderDate(LocalDateTime.now()); // 결제 완료 시점의 주문일시 업데이트
        System.out.println("Mock Order " + orderId + " payment successful. Status: " + order.getStatus());

        // 4. 데이터 플랫폼으로 주문 정보 전송 (Mock)
        paymentService.sendOrderInfoToDataPlatform(order.getOrderId(), order.getOrderDate());

        // 5. 인기 판매 상품 판매량 업데이트 (Mock)
        orderItemsByOrder.get(order.getOrderId()).forEach(item ->
            ((ProductServiceImpl) productService).addSalesCount(item.getProductId(), item.getQuantity())
        );

        return order;
    }

    @Override
    public Optional<Order> getOrder(Long orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setOrderItems(orderItemsByOrder.getOrDefault(orderId, new ArrayList<>()));
        }
        return Optional.ofNullable(order);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orders.values().stream()
                .filter(order -> order.getUserId().equals(userId))
                .peek(order -> order.setOrderItems(orderItemsByOrder.getOrDefault(order.getOrderId(), new ArrayList<>())))
                .collect(Collectors.toList());
    }

    @Override
    public boolean cancelOrder(Long orderId) {
        Order order = orders.get(orderId);
        if (order == null || order.getStatus() == OrderStatus.CANCELED || order.getStatus() == OrderStatus.REFUNDED) {
            return false; // 취소 불가능한 상태
        }

        // 이미 결제 완료된 주문이라면 환불 로직 필요
        if (order.getStatus() == OrderStatus.PAID) {
            // 잔액 환불
            balanceService.topUpBalance(order.getUserId(), order.getTotalAmount());
            paymentService.refundPayment(order.getOrderId(), order.getTotalAmount());
            System.out.println("Mock Order " + orderId + " refunded. Amount: " + order.getTotalAmount());
            order.setStatus(OrderStatus.REFUNDED); // 환불 완료 상태로 변경
        } else {
            order.setStatus(OrderStatus.CANCELED); // 결제 전이라면 단순히 취소 상태
        }

        // 재고 원복
        orderItemsByOrder.get(orderId).forEach(item -> productService.increaseStock(item.getProductId(), item.getQuantity()));

        // 사용된 쿠폰 원복
        if (order.getUserCouponId() != null) {
            couponService.cancelCouponUsage(order.getUserCouponId());
        }

        System.out.println("Mock Order " + orderId + " cancelled. New status: " + order.getStatus());
        return true;
    }
}

