package kr.hhplus.be.server.ecommerce.domain.coupon;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon {
    private Long userCouponId;
    private Long userId;
    private Long couponId; // 어떤 Coupon에 대한 UserCoupon인지
    private LocalDateTime issuedAt; // 발급 일시
    private LocalDateTime usedAt; // 사용 일시 (null이면 미사용)
    private boolean isUsed; // 사용 여부 (true: 사용됨, false: 미사용)

    // 편의 메서드 (Mocking을 위해 간단히)
    public boolean isUsable(LocalDateTime now, Coupon coupon) {
        return !isUsed && coupon.isValid(now);
    }
}
