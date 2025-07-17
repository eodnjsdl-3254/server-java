package kr.hhplus.be.server.ecommerce.service.user.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.ecommerce.domain.user.BalanceTransaction;
import kr.hhplus.be.server.ecommerce.domain.user.BalanceTransactionType;
import kr.hhplus.be.server.ecommerce.domain.user.UserBalance;
import kr.hhplus.be.server.ecommerce.service.user.IBalanceService;

@Service
public class BalanceServiceImpl implements IBalanceService {

    // 인메모리 데이터 저장소 (Mocking용)
    private final Map<Long, UserBalance> userBalances = new ConcurrentHashMap<>();
    private final Map<Long, List<BalanceTransaction>> balanceTransactions = new ConcurrentHashMap<>();
    private final AtomicLong transactionIdCounter = new AtomicLong(0L); // 트랜잭션 ID 생성용

    // 초기 데이터 (예시)
    public BalanceServiceImpl() {
        // 사용자 1의 잔액 초기화 (예시)
        userBalances.put(1L, new UserBalance(1L, 1L, new BigDecimal("100000.00"), LocalDateTime.now()));
    }

    @Override
    public UserBalance topUpBalance(Long userId, BigDecimal amount) {
        // userId가 없으면 새로 생성, 있으면 기존 잔액에 더함
        userBalances.compute(userId, (key, existingBalance) -> {
            if (existingBalance == null) {
                // 새로운 사용자 잔액 생성 (balanceId는 userId와 동일하게 간단히 처리)
                existingBalance = new UserBalance(userId, userId, amount, LocalDateTime.now());
            } else {
                existingBalance.setAmount(existingBalance.getAmount().add(amount));
                existingBalance.setLastUpdatedAt(LocalDateTime.now());
            }
            return existingBalance;
        });
        recordTransaction(userId, amount, BalanceTransactionType.TOP_UP.name(), null);
        return userBalances.get(userId);
    }

    @Override
    public UserBalance deductBalance(Long userId, BigDecimal amount) {
        UserBalance balance = userBalances.get(userId);
        if (balance == null || balance.getAmount().compareTo(amount) < 0) {
            // 잔액 부족 또는 사용자 없음 시 예외 발생 (실제 구현에서는 Custom Exception)
            throw new IllegalArgumentException("Insufficient balance or user not found for userId: " + userId);
        }
        balance.setAmount(balance.getAmount().subtract(amount));
        balance.setLastUpdatedAt(LocalDateTime.now());
        recordTransaction(userId, amount.negate(), BalanceTransactionType.DEDUCT.name(), null); // 차감은 음수로 기록
        return balance;
    }

    @Override
    public UserBalance getBalance(Long userId) {
        return userBalances.getOrDefault(userId, new UserBalance(userId, userId, BigDecimal.ZERO, LocalDateTime.now()));
    }

    @Override
    public BalanceTransaction recordTransaction(Long userId, BigDecimal amount, String type, String referenceId) {
        BalanceTransaction transaction = new BalanceTransaction(
                transactionIdCounter.incrementAndGet(),
                userId,
                BalanceTransactionType.valueOf(type),
                amount,
                LocalDateTime.now(),
                referenceId
        );
        balanceTransactions.computeIfAbsent(userId, k -> new ArrayList<>()).add(transaction);
        return transaction;
    }

    @Override
    public List<BalanceTransaction> getBalanceTransactions(Long userId) {
        return balanceTransactions.getOrDefault(userId, new ArrayList<>());
    }
}
