package shop.mtcoding.bank.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.dto.ResponseDto;
import shop.mtcoding.bank.dto.user.UserReqDto.JoinReqDto;
import shop.mtcoding.bank.dto.user.UserRespDto.JoinRespDto;
import shop.mtcoding.bank.service.UserService;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;

  // /api/join 회원가입이기 때문에 인증이 필요없는 주소
  // json 형식으로 데이터 받기 위해 @RequestBody
  @PostMapping("/join")
  public ResponseEntity<?> join(@RequestBody @Valid JoinReqDto joinReqDto, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      Map<String, String> errorMap = new HashMap<>();

      for (FieldError error : bindingResult.getFieldErrors()) {
        errorMap.put(error.getField(), error.getDefaultMessage());
      }

      return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), HttpStatus.BAD_REQUEST);

    }

    JoinRespDto joinRespDto = userService.회원가입(joinReqDto);
    return new ResponseEntity<>(new ResponseDto<>(1, "회원가입", joinRespDto), HttpStatus.CREATED);
  }

  @GetMapping("/hello")
  public String hello() {
    return "hello";
  }

}
