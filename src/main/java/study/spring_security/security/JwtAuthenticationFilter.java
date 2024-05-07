package study.spring_security.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {

		try{
			final String authHeader = request.getHeader("Authorization");
			final String jwt;
			final String userUuid;

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				log.info("로그인이 되어있지 않음");
				filterChain.doFilter(request, response);
				return;
			}

			jwt = authHeader.substring(7);
			userUuid = jwtTokenProvider.getUuid(jwt);

			// SecurityContextHolder를 사용하여 현재 보안 컨텍스트에 인증 정보가 없는 경우 사용자의 인증 정보를 생성하고 설정
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userUuid);
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);

				// filterChain.doFilter(request, response);
			}
		}catch(Exception e){
			log.error("{}", e.getMessage());
			request.setAttribute("exception", e);
		}
		filterChain.doFilter(request, response);
	}

}
