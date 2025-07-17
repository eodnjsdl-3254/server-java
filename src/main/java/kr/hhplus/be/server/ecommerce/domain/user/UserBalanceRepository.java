package kr.hhplus.be.server.ecommerce.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {
    /**
     * 사용자 ID로 UserBalance 엔티티를 조회합니다.
     * 각 사용자는 하나의 잔액만 가진다고 가정하고 Optional로 반환합니다.
     * @param userId 조회할 사용자 ID
     * @return 해당하는 UserBalance 엔티티 (Optional로 감싸져 반환)
     */
    Optional<UserBalance> findByUserId(Long userId);
}

