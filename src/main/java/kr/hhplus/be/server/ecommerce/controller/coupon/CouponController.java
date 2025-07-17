package kr.hhplus.be.server.ecommerce.controller.coupon;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.ecommerce.controller.coupon.dto.CouponIssueRequest;
import kr.hhplus.be.server.ecommerce.domain.coupon.UserCoupon;
import kr.hhplus.be.server.ecommerce.service.coupon.ICouponService;
import lombok.RequiredArgsConstructor;

@Tag(name = "쿠폰 관리 API", description = "선착순 쿠폰 발급 및 사용자 보유 쿠폰 조회")
@RestController
@RequestMapping("/api/users/{userId}/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final ICouponService couponService;

    @Operation(summary = "선착순 쿠폰 발급", description = "특정 사용자에게 선착순 쿠폰을 발급합니다. (쿠폰 코드 필요)")
    @PostMapping("/issue")
    public ResponseEntity<UserCoupon> issueCoupon(
            @PathVariable Long userId,
            @RequestBody CouponIssueRequest request) {
        try {
            UserCoupon issuedCoupon = couponService.issueCoupon(userId, request.getCouponCode());
            return ResponseEntity.status(HttpStatus.CREATED).body(issuedCoupon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 쿠폰 코드 없거나 유효하지 않음
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // 쿠폰 소진 또는 이미 발급됨
        }
    }

    @Operation(summary = "사용자 보유 쿠폰 조회", description = "특정 사용자가 보유한 미사용 쿠폰 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<UserCoupon>> getUserCoupons(@PathVariable Long userId) {
        List<UserCoupon> userCoupons = couponService.getUserCoupons(userId);
        return ResponseEntity.ok(userCoupons);
    }
}
