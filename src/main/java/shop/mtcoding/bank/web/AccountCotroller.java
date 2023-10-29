package shop.mtcoding.bank.web;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.dto.ResponseDto;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountSaveRespDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountSaveRespDto.AccountListRespDto;
import shop.mtcoding.bank.service.AccountService;
import shop.mtcoding.bank.service.AccountService.AccountDepositReqDto;
import shop.mtcoding.bank.service.AccountService.AccountDepositRespDto;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AccountCotroller {
  private final AccountService accountService;

  @PostMapping("/s/account")
  public ResponseEntity<?> saveAccount(@RequestBody @Valid AccountSaveReqDto accountSaveReqDto,
      BindingResult bindingResult, @AuthenticationPrincipal LoginUser loginUser) {

    AccountSaveRespDto accountSaveRespDto = accountService.계좌등록(accountSaveReqDto, loginUser.getUser().getId());
    return new ResponseEntity<>(new ResponseDto<>(1, "계좌등록 성공", accountSaveRespDto), HttpStatus.CREATED);
  }

  // 인증이 필요하고, account 테이블에 login한 유저의 계좌만 주세요.
  @GetMapping(value = "/s/account/login-user")
  public ResponseEntity<?> findUserAccount(@AuthenticationPrincipal LoginUser loginUser) {

    AccountListRespDto accountListRespDto = accountService.계좌목록보기_유저별(loginUser.getUser().getId());
    return new ResponseEntity<>(new ResponseDto<>(1, "계좌목록보기_유저별 성공", accountListRespDto), HttpStatus.OK);
  }

  /*
   * 아래 코드는 불필요한 로직이 들어가 있다.
   */
  // 인증이 필요하고, account 테이블에 1번 row를 주세요!!
  // cos로 로그인을 했는데, cos의 id가 2번이에요!!
  // 권한 처리 때문에 선호하지 않는다.
  // @GetMapping(value = "/s/account/{id}")
  // public ResponseEntity<?> findUserAccount(@PathVariable Long id,
  // @AuthenticationPrincipal LoginUser loginUser) {

  // // 인증이 된 사용자만 여기로 들어올 수 있는데 권한 검사를 한다는 것은 쓸데없는 코드라고 생각한다.
  // if (id != loginUser.getUser().getId()) {
  // throw new CustomForbiddenException("권한이 없습니다.");
  // }

  // AccountListRespDto accountListRespDto = accountService.계좌목록보기_유저별(id);
  // return new ResponseEntity<>(new ResponseDto<>(1, "계좌목록보기_유저별 성공",
  // accountListRespDto), HttpStatus.OK);
  // }

  @DeleteMapping("/s/account/{number}")
  public ResponseEntity<?> deleteAccount(@PathVariable Long number, @AuthenticationPrincipal LoginUser loginUser) {
    accountService.계좌삭제(number, loginUser.getUser().getId());
    return new ResponseEntity<>(new ResponseDto<>(1, "계좌 삭제 완료", null), HttpStatus.OK);
  }

    // ATM 입금
    @PostMapping("/account/deposit")
    public ResponseEntity<?> depositAccount(@RequestBody @Valid AccountDepositReqDto accountDepositReqDto, BindingResult bindingResult) {
        AccountDepositRespDto accountDepositRespDto = accountService.계좌입금(accountDepositReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 입금 완료", accountDepositRespDto), HttpStatus.CREATED);
    }

}
