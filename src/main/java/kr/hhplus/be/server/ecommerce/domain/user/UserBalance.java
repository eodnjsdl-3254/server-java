package kr.hhplus.be.server.ecommerce.domain.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBalance {
    private Long balanceId;
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime lastUpdatedAt;
}