package kr.hhplus.be.server.ecommerce.service.user;

import java.math.BigDecimal;
import java.util.List;

import kr.hhplus.be.server.ecommerce.domain.user.BalanceTransaction;
import kr.hhplus.be.server.ecommerce.domain.user.UserBalance;

public interface IBalanceService {
    UserBalance topUpBalance(Long userId, BigDecimal amount); // 잔액 충전
    UserBalance deductBalance(Long userId, BigDecimal amount); // 잔액 차감 (주문/결제 시 호출)
    UserBalance getBalance(Long userId); // 잔액 조회
    BalanceTransaction recordTransaction(Long userId, BigDecimal amount, String type, String referenceId); // 잔액 거래 기록
    List<BalanceTransaction> getBalanceTransactions(Long userId); // 잔액 내역 기록 조회
}
