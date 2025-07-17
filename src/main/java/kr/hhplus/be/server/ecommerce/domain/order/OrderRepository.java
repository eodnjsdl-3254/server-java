package kr.hhplus.be.server.ecommerce.domain.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * 특정 사용자 ID의 모든 주문 내역을 조회합니다.
     * @param userId 조회할 사용자 ID
     * @return 해당 사용자의 주문 리스트
     */
    List<Order> findByUserId(Long userId);

    /**
     * 특정 사용자 ID의 주문 내역을 페이지네이션하여 조회합니다.
     * @param userId 조회할 사용자 ID
     * @param pageable 페이지네이션 정보
     * @return 해당 사용자의 주문 페이지
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);

    /**
     * 특정 주문 상태의 주문 목록을 조회합니다.
     * @param status 조회할 주문 상태 (OrderStatus ENUM)
     * @return 해당 상태의 주문 리스트
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * 특정 기간 내에 생성된 주문을 조회합니다.
     * @param startDate 시작 일시
     * @param endDate 종료 일시
     * @return 해당 기간 내에 생성된 주문 리스트
     */
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
