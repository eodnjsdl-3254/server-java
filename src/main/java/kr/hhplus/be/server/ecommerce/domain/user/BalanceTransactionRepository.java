package kr.hhplus.be.server.ecommerce.domain.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, Long> {
    /**
     * 특정 사용자 ID의 모든 거래 내역을 조회합니다.
     * @param userId 조회할 사용자 ID
     * @return 해당 사용자의 거래 내역 리스트
     */
    List<BalanceTransaction> findByUserId(Long userId);

    /**
     * 특정 사용자 ID의 거래 내역을 페이지네이션하여 조회합니다.
     * @param userId 조회할 사용자 ID
     * @param pageable 페이지네이션 정보
     * @return 해당 사용자의 거래 내역 페이지
     */
    Page<BalanceTransaction> findByUserId(Long userId, Pageable pageable);

    /**
     * 특정 사용자 ID와 거래 유형으로 거래 내역을 조회합니다.
     * @param userId 조회할 사용자 ID
     * @param type 조회할 거래 유형 (BalanceTransactionType ENUM)
     * @return 해당 조건에 맞는 거래 내역 리스트
     */
    List<BalanceTransaction> findByUserIdAndType(Long userId, BalanceTransactionType type);
}
