package shop.mtcoding.bank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	// @slf4j 어노테이션으로 사용시 junit 테스트에서 문제 발생
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Bean // Ioc 컨테이너에 BCryptPasswordEncoder() 객체가 등록됨.
	public BCryptPasswordEncoder passwordEncoder() {
		log.debug("디버그 : BCryptPasswordEncoder 빈 등록됨.");

		return new BCryptPasswordEncoder();
	}

	// JWT 서버를 만들 예정! Session 사용안 함.
	public SecurityFilterChain filerChain(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable(); // iframe 허용안함
		http.csrf().disable(); // csrf 허용안함 //
		// cors : js 허용을 안 하겠다.
		http.cors().configurationSource(null); // cors 재정의
	}
}
