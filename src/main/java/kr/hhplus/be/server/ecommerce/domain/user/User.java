package kr.hhplus.be.server.ecommerce.domain.user;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long userId;
    private String username;
    private String email;
    private String passwordHash; // 실제 구현에서는 암호화 필요
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
