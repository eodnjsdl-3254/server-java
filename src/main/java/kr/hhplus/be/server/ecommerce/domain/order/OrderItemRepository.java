package kr.hhplus.be.server.ecommerce.domain.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    /**
     * 특정 주문 ID에 속하는 모든 주문 상품을 조회합니다.
     * @param orderId 조회할 주문 ID
     * @return 해당 주문에 속하는 주문 상품 리스트
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * 특정 상품 ID를 포함하는 주문 상품을 조회합니다.
     * @param productId 조회할 상품 ID
     * @return 해당 상품 ID를 포함하는 주문 상품 리스트
     */
    List<OrderItem> findByProductId(Long productId);
}
