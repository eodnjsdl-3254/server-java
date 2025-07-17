package kr.hhplus.be.server.ecommerce.service.order;

import java.util.List;
import java.util.Optional;

import kr.hhplus.be.server.ecommerce.controller.order.dto.OrderItemRequest;
import kr.hhplus.be.server.ecommerce.domain.order.Order;

public interface IOrderService {
    Order createOrder(Long userId, List<OrderItemRequest> items, Long userCouponId); // 주문 생성
    Order processPayment(Long orderId, String paymentMethod); // 주문 결제 수행
    Optional<Order> getOrder(Long orderId); // 단일 주문 조회
    List<Order> getUserOrders(Long userId); // 사용자 주문 목록 조회
    boolean cancelOrder(Long orderId); // 주문 취소
}