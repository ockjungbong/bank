package shop.mtcoding.bank.domain.account;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.handler.ex.CustomApiException;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 4)
  private Long number; // 계좌번호

  @Column(nullable = false, length = 4)
  private Long password; // 계좌비밀번호

  @Column(nullable = false)
  private Long balance; // 잔액 (디폴트 값 1000원)

  // 한 명의 유저는 다수의 계좌를 가질 수 있기 때문에 ManyToOne
  // 항상 ORM에서 fk의 주인은 Many Entity이다.
  @ManyToOne(fetch = FetchType.LAZY) // account.getUser().아무필드호출() == Lazy 발동
  private User user;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Builder
  public Account(Long id, Long number, Long password, Long balance, User user,
      LocalDateTime updatedAt, LocalDateTime createdAt) {
    this.id = id;
    this.number = number;
    this.password = password;
    this.balance = balance;
    this.user = user;
    this.updatedAt = updatedAt;
    this.createdAt = createdAt;
  }

  public void checkOwner(Long userId) {
    String testUsername = user.getUsername();
    System.out.println("테스트 : " + testUsername);
    if (user.getId() != userId) { // Lazy 로딩이어도 id를 조회할 때는 select 쿼리가 날라가지 않는다.
      throw new CustomApiException("계좌 소유자가 아닙니다.");
    }
  }

public void deposit(Long amount) {
  balance = balance + amount;
}

}