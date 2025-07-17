package kr.hhplus.be.server.ecommerce.domain.coupon;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    /**
     * 쿠폰 코드로 Coupon 엔티티를 조회합니다. (쿠폰 코드는 고유해야 함)
     * @param couponCode 조회할 쿠폰 코드
     * @return 해당하는 Coupon 엔티티 (Optional로 감싸져 반환)
     */
    Optional<Coupon> findByCouponCode(String couponCode);

    /**
     * 이름에 특정 키워드가 포함된 쿠폰을 조회합니다.
     * @param name 검색할 쿠폰 이름 키워드
     * @return 키워드를 포함하는 쿠폰 리스트
     */
    List<Coupon> findByNameContaining(String name);

    /**
     * 만료일이 특정 일시 이후인 활성 쿠폰을 조회합니다.
     * @param expirationDate 조회 기준 만료일시
     * @return 해당 조건에 맞는 쿠폰 리스트
     */
    List<Coupon> findByExpirationDateAfter(LocalDateTime expirationDate);

    /**
     * 현재 발급 수량이 최대 발급 수량보다 작은 쿠폰을 조회합니다 (발급 가능한 쿠폰).
     * @return 발급 가능한 쿠폰 리스트
     */
    List<Coupon> findByCurrentUsageLessThanMaxUsage();
}
