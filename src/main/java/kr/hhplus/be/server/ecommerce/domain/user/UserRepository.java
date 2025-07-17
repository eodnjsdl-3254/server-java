package kr.hhplus.be.server.ecommerce.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 사용자 이름으로 User 엔티티를 조회합니다.
     * @param username 조회할 사용자 이름
     * @return 해당하는 User 엔티티 (Optional로 감싸져 반환)
     */
    Optional<User> findByUsername(String username);

    /**
     * 이메일로 User 엔티티를 조회합니다.
     * @param email 조회할 이메일 주소
     * @return 해당하는 User 엔티티 (Optional로 감싸져 반환)
     */
    Optional<User> findByEmail(String email);
}
