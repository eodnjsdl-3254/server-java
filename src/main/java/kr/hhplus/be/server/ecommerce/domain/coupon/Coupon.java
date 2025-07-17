package kr.hhplus.be.server.ecommerce.domain.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.hhplus.be.server.ecommerce.domain.CouponType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    private Long couponId;
    private String couponCode;
    private String name; // 쿠폰 이름 (예: 선착순 50% 할인 쿠폰)
    private CouponType couponType; // 쿠폰 유형 (정률 할인, 정액 할인)
    private BigDecimal discountValue; // 할인율 (0.0 ~ 1.0) 또는 할인 금액
    private Integer maxUsage; // 총 발급 가능 수량
    private Integer currentUsage; // 현재까지 발급된 수량
    private LocalDateTime expirationDate; // 쿠폰 만료일시
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 편의 메서드 (Mocking을 위해 간단히)
    public boolean isValid(LocalDateTime now) {
        return now.isBefore(expirationDate) && (currentUsage < maxUsage);
    }
}
