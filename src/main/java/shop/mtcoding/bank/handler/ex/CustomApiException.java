package shop.mtcoding.bank.handler.ex;

// 나만의 Exception 만들기
public class CustomApiException extends RuntimeException{
	
	public CustomApiException(String message) {
		super(message);
	}
}
