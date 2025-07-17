package kr.hhplus.be.server.ecommerce.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long orderId;
    private Long userId;
    private BigDecimal totalAmount; // 총 결제 금액
    private LocalDateTime orderDate;
    private OrderStatus status; // 주문 상태 (PENDING, PAID, CANCELED 등)
    private Long userCouponId; // 사용된 쿠폰 ID (nullable)
    private List<OrderItem> orderItems; // 주문 상품 목록

    // 계산 로직 (Mocking을 위해 간단히)
    public BigDecimal calculateTotalAmount() {
        if (orderItems == null) return BigDecimal.ZERO;
        return orderItems.stream()
                .map(item -> item.getItemPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}