package shop.mtcoding.bank.domain.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

  // jpa query method
  // checkpoint : 리팩토링 해야 함
  Optional<Account> findByNumber(Long number);

}
