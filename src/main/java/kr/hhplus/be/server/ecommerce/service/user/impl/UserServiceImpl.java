package kr.hhplus.be.server.ecommerce.service.user.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.hhplus.be.server.ecommerce.domain.user.User;
import kr.hhplus.be.server.ecommerce.domain.user.UserRepository;
import kr.hhplus.be.server.ecommerce.service.user.IUserService;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository; // UserRepository 주입
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입

    // 생성자 주입 (권장 방식)
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(String username, String email, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }

        String hashedPassword = passwordEncoder.encode(password);

        User newUser = User.builder()
                            .username(username)
                            .email(email)
                            .passwordHash(hashedPassword)
                            // createdAt, updatedAt은 @PrePersist로 자동 설정될 예정
                            .build();

        User savedUser = userRepository.save(newUser);
        System.out.println("User registered and saved to DB: " + savedUser.getUsername() + " with ID: " + savedUser.getUserId());
        return savedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> login(String username, String password) {
        // 1. 사용자 이름으로 User 조회
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            // 사용자가 존재하지 않음 (username이 틀린 경우)
            return Optional.empty();
        }

        User user = userOptional.get();

        // 2. 입력된 비밀번호와 저장된 해시 비밀번호 비교
        if (passwordEncoder.matches(password, user.getPasswordHash())) {
            // 비밀번호 일치: 로그인 성공
            System.out.println("User logged in: " + username);
            return Optional.of(user);
        } else {
            // 비밀번호 불일치: 로그인 실패
            System.out.println("Login failed for user: " + username + " - Incorrect password.");
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getProfile(Long userId) {
        // userId로 User 조회
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            System.out.println("User profile retrieved for ID: " + userId);
        } else {
            System.out.println("User profile not found for ID: " + userId);
        }
        return userOptional;
    }
}
