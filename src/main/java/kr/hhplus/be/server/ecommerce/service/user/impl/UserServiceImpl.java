package kr.hhplus.be.server.ecommerce.service.user.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.ecommerce.domain.user.User;
import kr.hhplus.be.server.ecommerce.service.user.IUserService;

@Service
public class UserServiceImpl implements IUserService {

    // 인메모리 데이터 저장소 (Mocking용)
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final Map<String, User> usersByUsername = new ConcurrentHashMap<>(); // 사용자명으로 조회 용이
    private final AtomicLong userIdCounter = new AtomicLong(0L); // 사용자 ID 생성용

    // 초기 Mock 사용자 데이터
    public UserServiceImpl() {
        // ID 1L 사용자 추가
        User initialUser = new User(userIdCounter.incrementAndGet(), "testuser", "test@example.com", "password123", LocalDateTime.now(), LocalDateTime.now());
        users.put(initialUser.getUserId(), initialUser);
        usersByUsername.put(initialUser.getUsername(), initialUser);
    }

    @Override
    public User register(String username, String email, String password) {
        if (usersByUsername.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        Long newUserId = userIdCounter.incrementAndGet();
        User newUser = new User(newUserId, username, email, password, LocalDateTime.now(), LocalDateTime.now()); // 실제로는 passwordHash를 사용
        users.put(newUserId, newUser);
        usersByUsername.put(username, newUser);
        System.out.println("Mock User registered: " + username + " with ID: " + newUserId);
        return newUser;
    }

    @Override
    public User login(String username, String password) {
        User user = usersByUsername.get(username);
        if (user != null && user.getPasswordHash().equals(password)) { // 실제로는 비밀번호 해싱 및 검증
            System.out.println("Mock User logged in: " + username);
            return user;
        }
        throw new IllegalArgumentException("Invalid username or password");
    }

    @Override
    public User getProfile(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        return user;
    }
}
