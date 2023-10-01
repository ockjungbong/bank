package shop.mtcoding.bank.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountSaveRespDto;
import shop.mtcoding.bank.handler.ex.CustomApiException;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends DummyObject {

  // 가짜 레퍼지토리가 이 서비스에 주입이 된다.
  @Mock
  private UserRepository userRepository;

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks // 모든 Mock들이 InjectMocks로 주입됨.
  private AccountService accountService;

  @Spy // 진짜 객체를 InjectMocks에 주입한다.
  private ObjectMapper om;

  @Test
  public void 계좌등록_test() throws Exception {
    // given
    Long userId = 1L;
    AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
    accountSaveReqDto.setNumber(1111L);
    accountSaveReqDto.setPassword(1234L);

    // 계좌등록 메소드가 3단계로 처리 되므로 stub도 3단계로 만든다.
    // stub1
    User ssar = newMockUser(userId, "ssar", "쌀");
    when(userRepository.findById(ssar.getId())).thenReturn(Optional.of(ssar));

    // stub2
    when(accountRepository.findByNumber(1111L)).thenReturn(Optional.empty());

    // stub 3
    Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
    when(accountRepository.save(any())).thenReturn(ssarAccount);

    // when
    AccountSaveRespDto accountSaveRespDto = accountService.계좌등록(accountSaveReqDto, userId);
    String reponseBody = om.writeValueAsString(accountSaveRespDto);
    System.out.println("테스트 : " + reponseBody);

    // then
    assertThat(accountSaveRespDto.getNumber()).isEqualTo(1111);

  }

  @Test
  public void 계좌삭제_test() throws Exception {
    // given
    Long number = 1111L;
    Long userId = 2L;

    // stub
    User ssar = newMockUser(1L, "ssar", "쌀");
    Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
    // 가정
    when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount));

    // when
    assertThrows(CustomApiException.class, () -> accountService.계좌삭제(number, userId));
  }

}
