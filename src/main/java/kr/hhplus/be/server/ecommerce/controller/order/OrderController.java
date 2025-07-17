package kr.hhplus.be.server.ecommerce.controller.order;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.ecommerce.controller.order.dto.OrderRequest;
import kr.hhplus.be.server.ecommerce.controller.order.dto.PaymentRequest;
import kr.hhplus.be.server.ecommerce.domain.order.Order;
import kr.hhplus.be.server.ecommerce.service.order.IOrderService;
import kr.hhplus.be.server.ecommerce.service.order.impl.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;

@Tag(name = "주문 및 결제 API", description = "상품 주문 및 결제 처리")
@RestController
@RequestMapping("/api/users/{userId}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PaymentServiceImpl paymentServiceImpl;

    private final IOrderService orderService;

    OrderController(PaymentServiceImpl paymentServiceImpl) {
        this.paymentServiceImpl = paymentServiceImpl;
    }

    @Operation(summary = "주문 생성", description = "사용자 ID와 주문 상품 목록을 받아 주문을 생성합니다. (결제 대기 상태)")
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @PathVariable Long userId,
            @RequestBody OrderRequest request) {
        try {
            // userCouponId는 Optional이므로 null 체크
            Order newOrder = orderService.createOrder(userId, request.getItems(), request.getUserCouponId());
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        } catch (IllegalArgumentException e) {
            System.err.println("Order creation failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(null); // 재고 부족, 상품 없음, 쿠폰 유효하지 않음 등
        } catch (IllegalStateException e) {
            System.err.println("Order creation failed due to state: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // 동시성 문제 등
        }
    }

    @Operation(summary = "주문 결제", description = "특정 주문에 대해 결제를 수행합니다. (잔액 기반)")
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<Order> payOrder(
            @PathVariable Long userId, // userId는 경로에서 받지만, 실제 로직에서는 orderId로만 충분할 수 있음
            @PathVariable Long orderId,
            @RequestBody PaymentRequest request) {
        try {
            Order paidOrder = orderService.processPayment(orderId, request.getPaymentMethod());
            return ResponseEntity.ok(paidOrder);
        } catch (IllegalArgumentException e) {
            System.err.println("Payment failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(null); // 잔액 부족, 주문 상태 불일치 등
        }
    }

    @Operation(summary = "단일 주문 조회", description = "특정 주문 ID로 주문 상세 정보를 조회합니다.")
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long userId, // 실제 인증 로직에서는 userId가 PathVariable로 오는 대신 토큰 등으로 받아와야 함
            @PathVariable Long orderId) {
        Optional<Order> order = orderService.getOrder(orderId);
        if (order.isPresent() && order.get().getUserId().equals(userId)) { // 요청한 사용자의 주문인지 확인
            return ResponseEntity.ok(order.get());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "사용자 주문 목록 조회", description = "특정 사용자의 모든 주문 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "주문 취소", description = "특정 주문을 취소합니다. 결제 완료된 주문은 환불 처리됩니다.")
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long userId,
            @PathVariable Long orderId) {
        Optional<Order> orderOptional = orderService.getOrder(orderId);
        if (orderOptional.isEmpty() || !orderOptional.get().getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found or not belongs to user.");
        }
        try {
            boolean success = orderService.cancelOrder(orderId);
            if (success) {
                return ResponseEntity.ok("Order " + orderId + " cancelled successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to cancel order " + orderId + " (already cancelled/refunded or invalid state).");
            }
        } catch (Exception e) {
            System.err.println("Order cancellation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during cancellation.");
        }
    }
}
