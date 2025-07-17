package kr.hhplus.be.server.ecommerce.controller.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueRequest {
    private String couponCode;
}
