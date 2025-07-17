package kr.hhplus.be.server.ecommerce.domain.coupon;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.hhplus.be.server.ecommerce.domain.CouponType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupons")
public class Coupon {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;
	@Column(name = "coupon_code", unique = true, nullable = false, length = 100)
    private String couponCode;
	@Column(nullable = false, length = 255)
    private String name; // 쿠폰 이름 (예: 선착순 50% 할인 쿠폰)
	@Enumerated(EnumType.STRING) // CouponType이 Enum이라면
    @Column(name = "coupon_type", nullable = false)
    private CouponType couponType; // 쿠폰 유형 (정률 할인, 정액 할인)
	@Column(name = "discount_value", nullable = false, precision = 19, scale = 4) // 할인율은 보통 4자리까지
    private BigDecimal discountValue; // 할인율 (0.0 ~ 1.0) 또는 할인 금액
	@Column(name = "max_usage", nullable = false)
    private Integer maxUsage; // 총 발급 가능 수량
	@Column(name = "current_usage", nullable = false)
    private Integer currentUsage; // 현재까지 발급된 수량
	@Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate; // 쿠폰 만료일시
	@Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
	@Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 편의 메서드 (Mocking을 위해 간단히)
	@Transient
    public boolean isValid(LocalDateTime now) {
        return now.isBefore(expirationDate) && (currentUsage < maxUsage);
    }
}
