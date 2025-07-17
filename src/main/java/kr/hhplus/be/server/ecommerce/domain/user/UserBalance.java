package kr.hhplus.be.server.ecommerce.domain.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "user_balances")
public class UserBalance {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long balanceId;
	@Column(nullable = false, unique = true)
    private Long userId;
	@Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;
	@Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;
}