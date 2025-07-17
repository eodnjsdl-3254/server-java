package kr.hhplus.be.server.ecommerce.domain.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceTransaction {
    private Long transactionId;
    private Long userId;
    private BalanceTransactionType type; // ENUM으로 정의
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String referenceId;
}