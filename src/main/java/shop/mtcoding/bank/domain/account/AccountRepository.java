package shop.mtcoding.bank.domain.account;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

  // jpa query method
  // checkpoint : 리팩토링 해야 함(계좌 소유자 확인시에 쿼리가 두 번 나가지 때문에 join fetch)
  Optional<Account> findByNumber(Long number);

  // jpa query method
  // select * from account where user_id = :id;
  List<Account> findByUser_id(Long id);
}
