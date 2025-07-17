package kr.hhplus.be.server.ecommerce.controller.user;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.ecommerce.controller.user.dto.BalanceTopUpRequest;
import kr.hhplus.be.server.ecommerce.domain.user.UserBalance;
import kr.hhplus.be.server.ecommerce.service.user.IBalanceService;
import lombok.RequiredArgsConstructor;

@Tag(name = "잔액 관리 API", description = "사용자 잔액 충전 및 조회")
@RestController
@RequestMapping("/api/users/{userId}/balance")
@RequiredArgsConstructor // Lombok을 이용한 final 필드 자동 주입
public class BalanceController {

    private final IBalanceService balanceService;

    @Operation(summary = "잔액 충전", description = "특정 사용자의 잔액을 충전합니다.")
    @PostMapping("/top-up")
    public ResponseEntity<UserBalance> topUpBalance(
            @PathVariable Long userId,
            @RequestBody BalanceTopUpRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().build(); // 유효하지 않은 금액
        }
        UserBalance updatedBalance = balanceService.topUpBalance(userId, request.getAmount());
        return ResponseEntity.ok(updatedBalance);
    }

    @Operation(summary = "잔액 조회", description = "특정 사용자의 현재 잔액을 조회합니다.")
    @GetMapping
    public ResponseEntity<UserBalance> getBalance(@PathVariable Long userId) {
        UserBalance balance = balanceService.getBalance(userId);
        return ResponseEntity.ok(balance);
    }
}
