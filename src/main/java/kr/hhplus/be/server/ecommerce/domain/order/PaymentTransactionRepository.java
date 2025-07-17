package kr.hhplus.be.server.ecommerce.domain.order;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    /**
     * 특정 주문 ID에 대한 결제 내역을 조회합니다.
     * 주문과 결제가 1:1 관계라면 Optional을 반환합니다.
     * @param orderId 조회할 주문 ID
     * @return 해당하는 PaymentTransaction 엔티티 (Optional로 감싸져 반환)
     */
    Optional<PaymentTransaction> findByOrderId(Long orderId);

    /**
     * 특정 결제 상태의 결제 내역을 조회합니다.
     * @param status 조회할 결제 상태 (PaymentStatus ENUM)
     * @return 해당 상태의 결제 내역 리스트
     */
    List<PaymentTransaction> findByStatus(PaymentStatus status);

    /**
     * 특정 결제 수단으로 이루어진 결제 내역을 조회합니다.
     * @param paymentMethod 조회할 결제 수단
     * @return 해당 결제 수단의 결제 내역 리스트
     */
    List<PaymentTransaction> findByPaymentMethod(String paymentMethod);
}
