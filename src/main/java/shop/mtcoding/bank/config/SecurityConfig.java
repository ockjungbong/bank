package shop.mtcoding.bank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import shop.mtcoding.bank.domain.user.UserEnum;

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
	@Bean
	public SecurityFilterChain filerChain(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable(); // iframe 허용안함
		http.csrf().disable(); // csrf 허용안함 enable이면 post맨 작동안함.
		// cors : js 허용을 안 하겠다.
		http.cors().configurationSource(null); // cors 재정의

		/*
		 * SessionCreationPolicy.STATELESS 클라이언트가 로그인 request 서버는 User 세션 저장 서버가
		 * response 세션값 사라짐. (즉 유지하지 않음)
		 */
		// jSessionId를 서버 쪽에서 관리 안 하겠다는 의미
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.formLogin().disable();
		http.httpBasic().disable(); // httpBasic()은 브라우저가 팝업창을 이용해서 사용자 인증을 진행한다.

		http.authorizeHttpRequests().antMatchers("/api/s/**").authenticated().antMatchers("/api/admin/**")
				.hasRole("" + UserEnum.ADMIN) // ROLE_ 안붙여도 됨
				.anyRequest().permitAll();

		return http.build();
	}

	public CorsConfigurationSource configurationSource() {
		log.debug("디버그 : SecurityConfig의 configurationSource");
		
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");		
		configuration.addAllowedOriginPattern("*"); // 프론트 서버의 주소 등록
		configuration.setAllowCredentials(true); 	// 클라이언트에서 쿠키 요청 허용

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
