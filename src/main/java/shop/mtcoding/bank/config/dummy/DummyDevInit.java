package shop.mtcoding.bank.config.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import shop.mtcoding.bank.domain.user.UserRepository;

@Configuration
public class DummyDevInit {

  @Profile("dev") // prod 모드에서는 실행되면 안된다.
  @Bean
  CommandLineRunner init(UserRepository userRepository) {
    return (args) -> {
      // 서버 실행시에 무조건 실행된다.

    };
  }

}
