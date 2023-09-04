package shop.mtcoding.bank.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.util.CustomDateUtil;

public class UserRespDto {

	@Getter
	@Setter
	public static class LoginRespDto {
		private Long id;
		private String username;
		private String createdAt;

		public LoginRespDto(User user) {
			this.id = user.getId();
			this.username = user.getUsername();
			this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
		}

	}

	@ToString
	@Getter
	@Setter
	public static class JoinRespDto {
		private Long id;
		private String username;
		// pw는 보안사항이므로 응답해 주면 안 됨.
		private String fullname;

		public JoinRespDto(User user) {
			this.id = user.getId();
			this.username = user.getUsername();
			this.fullname = user.getFullname();
		}
	}

}
