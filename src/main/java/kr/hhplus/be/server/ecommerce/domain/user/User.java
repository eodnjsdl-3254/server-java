package kr.hhplus.be.server.ecommerce.domain.user;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class User {
	@Id // 기본 키
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
	@Column(unique = true, nullable = false, length = 100)
    private String username;
	@Column(unique = true, nullable = false, length = 255)
    private String email;
	@Column(nullable = false, length = 255)
    private String passwordHash; // 실제 구현에서는 암호화 필요
	@Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
	@Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
	
	@PrePersist // 엔티티가 영속화(저장)되기 전에 호출
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate // 엔티티가 업데이트되기 전에 호출
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
