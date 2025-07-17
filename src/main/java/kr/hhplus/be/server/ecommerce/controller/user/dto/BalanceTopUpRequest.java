package kr.hhplus.be.server.ecommerce.controller.user.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceTopUpRequest {
    private BigDecimal amount;
}
