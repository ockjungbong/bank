package shop.mtcoding.bank.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.domain.transaction.TransactionEnum;
import shop.mtcoding.bank.domain.transaction.TransactionRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountSaveRespDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountSaveRespDto.AccountListRespDto;
import shop.mtcoding.bank.handler.ex.CustomApiException;
import shop.mtcoding.bank.util.CustomDateUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountListRespDto 계좌목록보기_유저별(Long userId) {
        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new CustomApiException("유저를 찾을 수 없습니다."));

        // 유저의 모든 계좌목록
        List<Account> accountListPS = accountRepository.findByUser_id(userId);
        return new AccountListRespDto(userPS, accountListPS);

    }

    @Transactional
    public AccountSaveRespDto 계좌등록(AccountSaveReqDto accountSaveReqDto, Long userId) {
        // User가 DB에 있는지 검증
        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new CustomApiException("유저를 찾을 수 없습니다."));

        // 해당 계좌가 DB에 있는 중복여부를 체크
        Optional<Account> accountOP = accountRepository.findByNumber(accountSaveReqDto.getNumber());
        if (accountOP.isPresent()) {
            throw new CustomApiException("해당 계좌가 이미 존재합니다.");
        }

        // 계좌 등록
        Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(userPS));

        // DTO를 응답
        return new AccountSaveRespDto(accountPS);

    }

    @Transactional
    public void 계좌삭제(Long number, Long userId) {
        // 1. 계좌 확인
        Account accountPS = accountRepository.findByNumber(number).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다."));

        // 2. 계좌 소유자 확인
        // 익셉션이 터지면 핸들러를 통해서 처리가 되는 구조
        accountPS.checkOwner(userId);

        // 3. 계좌 삭제
        accountRepository.deleteById(accountPS.getId());
    }

    @Transactional
    public AccountDepositRespDto 계좌입금(AccountDepositReqDto accountDepositReqDto) {
        // 1. 0원 체크
        if(accountDepositReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        // 2. 입금 계좌 확인
        Account depositAccountPS = accountRepository.findByNumber(accountDepositReqDto.getNumber())
                    .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

        // 3. 입금(해당 계좌의 balance 조정 - update문 - 더티체킹)
        depositAccountPS.deposit(accountDepositReqDto.getAmount());

        // 거래내역 남기기
        Transaction transaction = Transaction.builder()
                    .depositAccount(depositAccountPS)
                    .withdrawAccount(null)
                    .depositAccountBalance(depositAccountPS.getBalance())
                    .withdrawAccountBalance(null)
                    .amount(accountDepositReqDto.getAmount())
                    .gubun(TransactionEnum.DEPOSIT)
                    .sender("ATM")
                    .receiver(accountDepositReqDto.getNumber()+"")
                    .tel(accountDepositReqDto.getTel())
                    .build();

        Transaction transationPS = transactionRepository.save(transaction);  
        
        return new AccountDepositRespDto(depositAccountPS, transationPS);

    }

    @Getter
    @Setter
    public static class AccountDepositRespDto {
        private Long id;        // 계좌Id
        private Long number;    // 계좌번호
        private TransationDto transaction;

        
        public AccountDepositRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.transaction = new TransationDto(transaction); // transaction에 있는 값을 TransationDto로 복사해서 할당한다.
        }


        @Getter
        @Setter
        public class TransationDto {
            private Long id;
            private String gubun;
            private String sender;
            private String receiver;
            private Long amount;
            @JsonIgnore
            private Long depositAccountBalance;  // 클라이언트에게 전달 X -> 서비스단에서 테스트 용도
            private String tel;
            private String createdAt;

            public TransationDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.tel = transaction.getTel();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Getter
    @Setter
    public static class AccountDepositReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;

        @NotNull
        private Long amount;

        @NotEmpty
        @Pattern(regexp = "^(DEPOSIT)$")
        private String gubun;

        @NotEmpty
        @Pattern(regexp = "^[0-9]{3}[0-9]{4}[0-9]{4}$")
        private String tel;

    }

}
