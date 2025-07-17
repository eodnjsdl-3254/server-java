package kr.hhplus.be.server.ecommerce.service.order.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.ecommerce.domain.order.PaymentStatus;
import kr.hhplus.be.server.ecommerce.domain.order.PaymentTransaction;
import kr.hhplus.be.server.ecommerce.service.order.IPaymentService;

@Service
public class PaymentServiceImpl implements IPaymentService {

    private final Map<Long, PaymentTransaction> paymentTransactions = new ConcurrentHashMap<>();
    private final AtomicLong paymentIdCounter = new AtomicLong(0L);

    @Override
    public PaymentTransaction initiatePayment(Long orderId, BigDecimal amount, String paymentMethod) {
        Long paymentId = paymentIdCounter.incrementAndGet();
        PaymentTransaction transaction = new PaymentTransaction(
                paymentId, orderId, amount, PaymentStatus.PENDING, paymentMethod, LocalDateTime.now(), null, "PAYMENT"
        );
        paymentTransactions.put(paymentId, transaction);
        System.out.println("Mock Payment initiated for Order ID: " + orderId + ", Amount: " + amount);
        return transaction;
    }

    @Override
    public PaymentTransaction confirmPayment(Long paymentId, String approvalCode) {
        PaymentTransaction transaction = paymentTransactions.get(paymentId);
        if (transaction == null || transaction.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("Payment not found or not in PENDING state for paymentId: " + paymentId);
        }
        transaction.setStatus(PaymentStatus.SUCCESS);
        transaction.setApprovalCode(approvalCode);
        transaction.setTransactionDate(LocalDateTime.now());
        System.out.println("Mock Payment confirmed for Payment ID: " + paymentId);
        // 실제라면 PG사 연동 로직
        return transaction;
    }

    @Override
    public PaymentTransaction failPayment(Long paymentId) {
        PaymentTransaction transaction = paymentTransactions.get(paymentId);
        if (transaction == null || transaction.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("Payment not found or not in PENDING state for paymentId: " + paymentId);
        }
        transaction.setStatus(PaymentStatus.FAILED);
        transaction.setTransactionDate(LocalDateTime.now());
        System.out.println("Mock Payment failed for Payment ID: " + paymentId);
        return transaction;
    }

    @Override
    public PaymentTransaction refundPayment(Long orderId, BigDecimal amount) {
        // 실제로는 해당 orderId에 대한 성공한 PaymentTransaction을 찾아 환불 처리
        // Mock에서는 단순히 환불 거래를 기록
        Long paymentId = paymentIdCounter.incrementAndGet();
        PaymentTransaction refundTransaction = new PaymentTransaction(
                paymentId, orderId, amount.negate(), PaymentStatus.SUCCESS, "BALANCE", LocalDateTime.now(), "REFUND_" + orderId, "REFUND"
        );
        paymentTransactions.put(paymentId, refundTransaction);
        System.out.println("Mock Refund processed for Order ID: " + orderId + ", Amount: " + amount);
        return refundTransaction;
    }

    @Override
    public void sendOrderInfoToDataPlatform(Long orderId, LocalDateTime paymentDate) {
        // Mock API 요구사항: 데이터 플랫폼으로의 전송 기능
        // 실제로는 Kafka, RabbitMQ 등의 메시지 큐 또는 HTTP 통신으로 데이터 플랫폼에 전송
        System.out.println("--- Mock Data Platform Integration ---");
        System.out.println("Order ID " + orderId + " payment info sent to data platform at " + paymentDate);
        System.out.println("--- End Mock Data Platform Integration ---");
    }
}
