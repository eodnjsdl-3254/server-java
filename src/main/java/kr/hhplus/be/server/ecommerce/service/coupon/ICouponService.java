package kr.hhplus.be.server.ecommerce.service.coupon;

import java.util.List;
import java.util.Optional;

import kr.hhplus.be.server.ecommerce.domain.coupon.Coupon;
import kr.hhplus.be.server.ecommerce.domain.coupon.UserCoupon;

public interface ICouponService {
    UserCoupon issueCoupon(Long userId, String couponCode); // 선착순 쿠폰 발급
    Optional<UserCoupon> useCoupon(Long userCouponId); // 사용자 쿠폰 사용 처리 (주문/결제 시 호출)
    void cancelCouponUsage(Long userCouponId); // 사용자 쿠폰 취소 (주문 취소/환불 시 호출)
    List<UserCoupon> getUserCoupons(Long userId); // 사용자 보유 쿠폰 조회
    Optional<Coupon> getCouponById(Long couponId); // 쿠폰 정보 조회
}
