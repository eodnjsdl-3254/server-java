package kr.hhplus.be.server.ecommerce.domain.order;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
	@Column(nullable = false)
    private Long userId;
	@Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
	private BigDecimal totalAmount; // 총 결제 금액
	@Column(name = "order_date", nullable = false, updatable = false)
    private LocalDateTime orderDate;
	@Enumerated(EnumType.STRING) // OrderStatus가 Enum이라면
    @Column(nullable = false)
    private OrderStatus status; // 주문 상태 (PENDING, PAID, CANCELED 등)
	@Column(name = "user_coupon_id")
    private Long userCouponId; // 사용된 쿠폰 ID (nullable)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // Order가 삭제되면 OrderItem도 삭제
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems; // 주문 상품 목록

    // 계산 로직 (Mocking을 위해 간단히)
	@Transient
    public BigDecimal calculateTotalAmount() {
        if (orderItems == null) return BigDecimal.ZERO;
        return orderItems.stream()
                .map(item -> item.getItemPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}