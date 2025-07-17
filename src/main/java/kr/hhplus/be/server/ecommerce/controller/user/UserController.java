package kr.hhplus.be.server.ecommerce.controller.user;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.ecommerce.controller.user.dto.UserRequest;
import kr.hhplus.be.server.ecommerce.domain.user.User;
import kr.hhplus.be.server.ecommerce.service.user.IUserService;
import lombok.RequiredArgsConstructor;

@Tag(name = "사용자 관리 API", description = "사용자 등록 및 로그인")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRequest request) {
        try {
            User newUser = userService.register(request.getUsername(), request.getEmail(), request.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 사용자명 중복 등의 오류
        }
    }

    @Operation(summary = "로그인", description = "사용자명과 비밀번호로 로그인합니다.")
    @PostMapping("/users/login") // 경로를 /users/login으로 명확히
    public ResponseEntity<User> loginUser(@Valid @RequestBody UserRequest request) { // @Valid 추가
        // 서비스에서 Optional<User>를 반환하므로, 이를 처리해야 합니다.
        return userService.login(request.getUsername(), request.getPassword())
                .map(user -> ResponseEntity.ok(user)) // 로그인 성공: 200 OK와 User 객체 반환
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()); // 로그인 실패: 401 Unauthorized 반환
    }

    @Operation(summary = "사용자 프로필 조회", description = "특정 사용자 ID로 프로필을 조회합니다.")
    @GetMapping("/users/{userId}/profile") // 경로를 명확히 위해 /users 추가
    public ResponseEntity<User> getUserProfile(@PathVariable Long userId) {
        // Optional<User>를 받아와서, 값이 있으면 User 객체를 사용하고, 없으면 Not Found 반환
        return userService.getProfile(userId)
                .map(user -> ResponseEntity.ok(user)) // Optional 안에 User가 있으면 200 OK와 User 객체 반환
                .orElseGet(() -> ResponseEntity.notFound().build()); // Optional이 비어있으면 404 Not Found 반환
    }
}
