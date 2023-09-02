package shop.mtcoding.bank.config.dummy;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserEnum;

public class DummyObject {
	// 엔티티에서 save용
	protected User newUser(String usernmae, String fullname) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode("1234");

		return User.builder()
				.username(usernmae)
				.password(encPassword)
				.email(usernmae + "@nate.com")
				.fullname(fullname)
				.role(UserEnum.CUSTOMER)
				.build();

	}

	// Mock 객체 테스트용
	protected User newMockUser(Long id, String usernmae, String fullname) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode("1234");

		return User.builder()
				.id(id)
				.username(usernmae)
				.password(encPassword)
				.email(usernmae + "@nate.com")
				.fullname(fullname)
				.role(UserEnum.CUSTOMER)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();

	}

}
