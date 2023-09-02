package shop.mtcoding.bank.dto.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserEnum;

public class UserReqDto {
  @Getter
  @Setter
  public static class JoinReqDto {
    // 유효성 검사
    private String username;
    private String password;
    private String email;
    private String fullname;

    // 나열된 필드값으로 엔티티 생성
    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
      return User.builder().username(username).password(passwordEncoder.encode(password)).email(email)
          .fullname(fullname).role(UserEnum.CUSTOMER).build();
    }
  }
}
