package kr.hhplus.be.server.ecommerce.domain.coupon;

import java.beans.Transient;
import java.time.LocalDateTime;

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
@Table(name = "user_coupons")
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCouponId;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long couponId; // 어떤 Coupon에 대한 UserCoupon인지
    @Column(name = "issued_at", nullable = false, updatable = false)
    private LocalDateTime issuedAt; // 발급 일시
    @Column(name = "used_at")
    private LocalDateTime usedAt; // 사용 일시 (null이면 미사용)
    @Column(name = "is_used", nullable = false)    
    private boolean isUsed; // 사용 여부 (true: 사용됨, false: 미사용)

    // 편의 메서드 (Mocking을 위해 간단히)
    @Transient
    public boolean isUsable(LocalDateTime now, Coupon coupon) {
        return !isUsed && coupon.isValid(now);
    }
}
