package kr.hhplus.be.server.ecommerce.controller.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
	@NotBlank(message = "사용자 이름은 필수입니다.")
	private String username;
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
