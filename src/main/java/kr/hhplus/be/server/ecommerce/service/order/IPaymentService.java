package kr.hhplus.be.server.ecommerce.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.hhplus.be.server.ecommerce.domain.order.PaymentTransaction;

public interface IPaymentService {
    PaymentTransaction initiatePayment(Long orderId, BigDecimal amount, String paymentMethod); // 결제 시작
    PaymentTransaction confirmPayment(Long paymentId, String approvalCode); // 결제 확인/성공
    PaymentTransaction failPayment(Long paymentId); // 결제 실패
    PaymentTransaction refundPayment(Long orderId, BigDecimal amount); // 결제 환불
    // Mock API 요구사항: 데이터 플랫폼으로의 전송 기능
    void sendOrderInfoToDataPlatform(Long orderId, LocalDateTime paymentDate);
}