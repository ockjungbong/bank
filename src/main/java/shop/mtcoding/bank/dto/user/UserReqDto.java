package shop.mtcoding.bank.dto.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserEnum;

public class UserReqDto {
  @Getter
  @Setter
  public static class JoinReqDto {
    @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해 주세요.")
    @NotEmpty // null이거나 공백일 수 없다.
    private String username;

    @NotEmpty
    @Size(min = 4, max = 20)
    private String password;

    @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 작성해 주세요.")
    @NotEmpty
    private String email;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z\uAC00-\uD7A3]{1,20}$", message = "한글/영문 1~20자 이내로 작성해 주세요.")
    private String fullname;

    // 나열된 필드값으로 엔티티 생성
    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
      return User.builder().username(username).password(passwordEncoder.encode(password)).email(email)
          .fullname(fullname).role(UserEnum.CUSTOMER).build();
    }
  }
}
