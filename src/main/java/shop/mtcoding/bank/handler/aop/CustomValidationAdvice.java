package shop.mtcoding.bank.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import shop.mtcoding.bank.dto.ResponseDto;
import shop.mtcoding.bank.handler.ex.CustomValidationException;

// 컨트롤러도 아니고 설정 파일도 아니기 때문에 
// @Component로 Ioc 컨테이너에 등록하자.
@Component
@Aspect
public class CustomValidationAdvice {

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
  public void postMapping() {
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
  public void putMapping() {
  }

  /*
   * - postMapping 또는 putMapping이 걸려 있는 모든 컨트롤러가 실행이 될 때 동작
   * - BindingResult라는 매개변수가 있고 에러가 있을 때,
   * - throw new CustomValidationException("유효성 검사 실패", errorMap); 발생
   */
  @Around("postMapping || putMapping") // joinPoint의 전후 제어
  public Object validationAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Object[] args = proceedingJoinPoint.getArgs(); // joinPoint의 매개변수

    for (Object arg : args) {
      if (arg instanceof BindingResult) {
        BindingResult bindingResult = (BindingResult) arg;

        if (bindingResult.hasErrors()) {
          Map<String, String> errorMap = new HashMap<>();

          for (FieldError error : bindingResult.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
          }

          throw new CustomValidationException("유효성 검사 실패", errorMap);

        }
      }

    }

    return proceedingJoinPoint.proceed(); // 정상적으로 해당 메서드를 실행해라!!
  }

}
