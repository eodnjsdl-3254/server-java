package kr.hhplus.be.server.ecommerce.domain.order;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private Long orderItemId;
    private Long orderId; // 어떤 Order에 속하는지
    private Long productId;
    private Integer quantity;
    private BigDecimal itemPrice; // 주문 시점의 상품 가격 (할인 미포함)

    public BigDecimal calculateItemTotal() {
        return itemPrice.multiply(BigDecimal.valueOf(quantity));
    }
}