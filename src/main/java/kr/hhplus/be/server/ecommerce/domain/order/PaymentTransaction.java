package kr.hhplus.be.server.ecommerce.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_transactions")
public class PaymentTransaction {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
	@Column(name = "order_id", nullable = false)
    private Long orderId;
	@Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
	@Enumerated(EnumType.STRING) // PaymentStatus가 Enum이라면
    @Column(nullable = false)
    private PaymentStatus status; // 결제 상태
	@Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod; // 결제 수단 (BALANCE, CARD 등)
	@Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
	@Column(name = "approval_code", length = 100) 
    private String approvalCode; // 승인 코드 (PG사 등)
	@Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType; // 거래 타입 (PAYMENT, REFUND)
}
