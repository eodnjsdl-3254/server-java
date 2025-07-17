package kr.hhplus.be.server.ecommerce.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction {
    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
    private PaymentStatus status; // 결제 상태
    private String paymentMethod; // 결제 수단 (BALANCE, CARD 등)
    private LocalDateTime transactionDate;
    private String approvalCode; // 승인 코드 (PG사 등)
    private String transactionType; // 거래 타입 (PAYMENT, REFUND)
}
