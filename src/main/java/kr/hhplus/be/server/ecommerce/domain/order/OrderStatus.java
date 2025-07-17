package kr.hhplus.be.server.ecommerce.domain.order;

public enum OrderStatus {
    PENDING,    // 결제 대기 중
    PAID,       // 결제 완료
    CANCELED,   // 주문 취소됨
    REFUNDED,   // 환불됨
    FAILED      // 결제 실패
}
