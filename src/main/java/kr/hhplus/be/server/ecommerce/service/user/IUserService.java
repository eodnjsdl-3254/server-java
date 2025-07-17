package kr.hhplus.be.server.ecommerce.service.user;

import java.util.Optional;

import kr.hhplus.be.server.ecommerce.domain.user.User;

public interface IUserService {
	User register(String username, String email, String password);
	Optional<User> login(String username, String password);
	Optional<User> getProfile(Long userId);
}
