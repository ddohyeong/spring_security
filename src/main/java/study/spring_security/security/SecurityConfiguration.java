package study.spring_security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final AuthenticationProvider authenticationProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.csrf(CsrfConfigurer::disable)  // CSRF 보호기능을 비활성화
				.authorizeHttpRequests( // Http 요청에 대한 접근을 제한
						authorizeHttpRequests -> authorizeHttpRequests
								// 해당 패턴은 URL 인증없이 접근을 허용
								.requestMatchers("/**").permitAll()
								// 다른 모든 요청은 인증을 요구
								.anyRequest().authenticated()
				)
				.sessionManagement(
						// 세션을 생성하지 않고 매 요청마다 인증을 수행하여 상태를 유지하지 않는 방식(STATELESS)
						// 이는 JWT 같은 토큰 기반 인증에 적합
						sessionManagement -> sessionManagement
								.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				// 사용자 정의 AuthenticationProvider 를 등록
				// 사용자의 인증 로직을 커스터마이징
				.authenticationProvider(authenticationProvider)
				// UsernamePasswordAuthenticationOnFilter 전에 처리함 -> JWT 토큰을 검증
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

}
