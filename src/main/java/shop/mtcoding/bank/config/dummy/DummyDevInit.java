package shop.mtcoding.bank.config.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;

@Configuration
public class DummyDevInit extends DummyObject {

  @Bean
  CommandLineRunner init(UserRepository userRepository) {
    return (args) -> {
      User ssar = userRepository.save(newUser("ssar", "쌀"));
      User cos = userRepository.save(newUser("cos", "코스,"));
      User love = userRepository.save(newUser("love", "러브"));
      User admin = userRepository.save(newUser("admin", "관리자"));
    };
  }
}