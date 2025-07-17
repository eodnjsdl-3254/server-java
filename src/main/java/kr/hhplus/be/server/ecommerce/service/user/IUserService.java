package kr.hhplus.be.server.ecommerce.service.user;

import kr.hhplus.be.server.ecommerce.domain.user.User;

public interface IUserService {
	User register(String username, String email, String password);
    User login(String username, String password);
    User getProfile(Long userId);
}
