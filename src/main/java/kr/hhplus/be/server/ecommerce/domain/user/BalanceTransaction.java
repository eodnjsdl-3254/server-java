package kr.hhplus.be.server.ecommerce.domain.user;

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
@Table(name = "balance_transactions")
public class BalanceTransaction {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
	@Column(nullable = false)
    private Long userId;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
    private BalanceTransactionType type; // ENUM으로 정의
	@Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;
	@Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
	@Column(name = "reference_id")
    private String referenceId;
}