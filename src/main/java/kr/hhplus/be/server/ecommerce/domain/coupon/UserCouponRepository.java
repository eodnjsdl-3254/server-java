package kr.hhplus.be.server.ecommerce.domain.coupon;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    /**
     * 특정 사용자 ID의 모든 사용자 쿠폰을 조회합니다.
     * @param userId 조회할 사용자 ID
     * @return 해당 사용자의 사용자 쿠폰 리스트
     */
    List<UserCoupon> findByUserId(Long userId);

    /**
     * 특정 사용자 ID와 쿠폰 ID로 사용자 쿠폰을 조회합니다. (중복 발급이 없다면 Optional)
     * @param userId 조회할 사용자 ID
     * @param couponId 조회할 쿠폰 ID
     * @return 해당 조건에 맞는 사용자 쿠폰 (Optional로 감싸져 반환)
     */
    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

    /**
     * 특정 사용자의 사용되지 않은 쿠폰을 조회합니다.
     * @param userId 조회할 사용자 ID
     * @param isUsed 사용 여부 (false로 설정하여 미사용 쿠폰 조회)
     * @return 해당 사용자의 미사용 쿠폰 리스트
     */
    List<UserCoupon> findByUserIdAndIsUsed(Long userId, boolean isUsed);

    /**
     * 특정 쿠폰 ID에 해당하는 모든 사용자 쿠폰을 조회합니다 (어떤 사용자들이 특정 쿠폰을 발급받았는지).
     * @param couponId 조회할 쿠폰 ID
     * @return 해당 쿠폰 ID에 해당하는 사용자 쿠폰 리스트
     */
    List<UserCoupon> findByCouponId(Long couponId);
}
