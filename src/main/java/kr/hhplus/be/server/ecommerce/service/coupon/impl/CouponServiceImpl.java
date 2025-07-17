package kr.hhplus.be.server.ecommerce.service.coupon.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.ecommerce.domain.CouponType;
import kr.hhplus.be.server.ecommerce.domain.coupon.Coupon;
import kr.hhplus.be.server.ecommerce.domain.coupon.UserCoupon;
import kr.hhplus.be.server.ecommerce.service.coupon.ICouponService;

@Service
public class CouponServiceImpl implements ICouponService {

    private final Map<Long, Coupon> coupons = new ConcurrentHashMap<>();
    private final Map<String, Long> couponCodeMap = new ConcurrentHashMap<>(); // 코드 -> ID 매핑
    private final AtomicLong couponIdCounter = new AtomicLong(0L);

    private final Map<Long, List<UserCoupon>> userCoupons = new ConcurrentHashMap<>(); // userId -> List<UserCoupon>
    private final Map<Long, UserCoupon> userCouponById = new ConcurrentHashMap<>(); // userCouponId -> UserCoupon
    private final AtomicLong userCouponIdCounter = new AtomicLong(0L);

    // 초기 Mock 쿠폰 데이터 설정
    public CouponServiceImpl() {
        // 선착순 100명 10% 할인 쿠폰
        Coupon coupon1 = new Coupon(couponIdCounter.incrementAndGet(), "FIRST10PERCENT", "선착순 10% 할인 쿠폰",
                CouponType.PERCENTAGE, new BigDecimal("0.10"), 100, 0,
                LocalDateTime.now().plusDays(7), LocalDateTime.now(), LocalDateTime.now());
        coupons.put(coupon1.getCouponId(), coupon1);
        couponCodeMap.put(coupon1.getCouponCode(), coupon1.getCouponId());

        // 선착순 50명 5000원 할인 쿠폰
        Coupon coupon2 = new Coupon(couponIdCounter.incrementAndGet(), "FIRST5000WON", "선착순 5000원 할인 쿠폰",
                CouponType.AMOUNT, new BigDecimal("5000.00"), 50, 0,
                LocalDateTime.now().plusDays(14), LocalDateTime.now(), LocalDateTime.now());
        coupons.put(coupon2.getCouponId(), coupon2);
        couponCodeMap.put(coupon2.getCouponCode(), coupon2.getCouponId());
    }

    @Override
    public synchronized UserCoupon issueCoupon(Long userId, String couponCode) {
        Long couponId = couponCodeMap.get(couponCode);
        if (couponId == null) {
            throw new IllegalArgumentException("Coupon code not found: " + couponCode);
        }

        Coupon coupon = coupons.get(couponId);
        if (coupon == null || !coupon.isValid(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired coupon: " + couponCode);
        }

        if (coupon.getCurrentUsage() >= coupon.getMaxUsage()) {
            throw new IllegalStateException("Coupon " + couponCode + " has been fully issued.");
        }

        // 이미 해당 쿠폰을 발급받았는지 확인 (중복 발급 방지)
        List<UserCoupon> existingUserCoupons = userCoupons.getOrDefault(userId, new ArrayList<>());
        boolean alreadyIssued = existingUserCoupons.stream()
                .anyMatch(uc -> uc.getCouponId().equals(couponId) && !uc.isUsed()); // 사용하지 않은 동일 쿠폰 확인

        if (alreadyIssued) {
            throw new IllegalStateException("User " + userId + " already has this coupon: " + couponCode);
        }

        coupon.setCurrentUsage(coupon.getCurrentUsage() + 1); // 발급 수량 증가
        coupon.setUpdatedAt(LocalDateTime.now());

        Long newUserCouponId = userCouponIdCounter.incrementAndGet();
        UserCoupon newUserCoupon = new UserCoupon(newUserCouponId, userId, couponId, LocalDateTime.now(), null, false);

        userCoupons.computeIfAbsent(userId, k -> new ArrayList<>()).add(newUserCoupon);
        userCouponById.put(newUserCouponId, newUserCoupon);

        System.out.println("Mock Coupon issued: " + couponCode + " to user " + userId + ". Remaining usage: " + (coupon.getMaxUsage() - coupon.getCurrentUsage()));
        return newUserCoupon;
    }

    @Override
    public Optional<UserCoupon> useCoupon(Long userCouponId) {
        UserCoupon userCoupon = userCouponById.get(userCouponId);
        if (userCoupon == null || userCoupon.isUsed()) {
            return Optional.empty(); // 존재하지 않거나 이미 사용됨
        }
        Coupon coupon = coupons.get(userCoupon.getCouponId());
        if (coupon == null || !coupon.isValid(LocalDateTime.now())) {
            return Optional.empty(); // 유효하지 않거나 만료된 쿠폰
        }

        userCoupon.setUsed(true);
        userCoupon.setUsedAt(LocalDateTime.now());
        System.out.println("Mock UserCoupon " + userCouponId + " used by user " + userCoupon.getUserId());
        return Optional.of(userCoupon);
    }

    @Override
    public void cancelCouponUsage(Long userCouponId) {
        UserCoupon userCoupon = userCouponById.get(userCouponId);
        if (userCoupon != null && userCoupon.isUsed()) {
            userCoupon.setUsed(false);
            userCoupon.setUsedAt(null); // 사용 취소
            // 쿠폰의 currentUsage를 감소시키는 로직은 실제 시스템에서는 중요하지만,
            // Mock에서는 선착순 쿠폰의 '발급 수량' 개념과 '사용 여부'를 분리하여 간단히 처리
            System.out.println("Mock UserCoupon " + userCouponId + " usage canceled.");
        }
    }

    @Override
    public List<UserCoupon> getUserCoupons(Long userId) {
        return userCoupons.getOrDefault(userId, new ArrayList<>()).stream()
                .filter(uc -> !uc.isUsed()) // 미사용 쿠폰만 조회
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Coupon> getCouponById(Long couponId) {
        return Optional.ofNullable(coupons.get(couponId));
    }
}
