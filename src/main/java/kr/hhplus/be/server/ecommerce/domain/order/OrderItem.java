package kr.hhplus.be.server.ecommerce.domain.order;

import java.beans.Transient;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @Column(name = "order_id", nullable = false) // 외래키로 사용될 컬럼
    private Long orderId; // 어떤 Order에 속하는지 (실제 Order 엔티티와의 @ManyToOne 관계 설정 필요)

    @Column(name = "product_id", nullable = false)
    private Long productId; // 실제 Product 엔티티와의 @ManyToOne 관계 설정 필요

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "item_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal itemPrice; // 주문 시점의 상품 가격 (할인 미포함)

    @Transient // 이 필드는 DB에 매핑하지 않음
    public BigDecimal calculateItemTotal() {
        return itemPrice.multiply(BigDecimal.valueOf(quantity));
    }
}