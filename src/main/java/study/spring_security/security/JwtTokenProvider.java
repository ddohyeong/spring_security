package study.spring_security.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service    // 어노테이션 등록
@RequiredArgsConstructor
public class JwtTokenProvider {
	private final Environment env;

	public String generateToken(Map<String, Objects> extractClaims, UserDetails userDetails) {
		return Jwts.builder()
				.setClaims(extractClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new java.util.Date(System.currentTimeMillis()))
				.setExpiration(new java.util.Date(System.currentTimeMillis() + env.getProperty("JWT.EXPIRATION_TIME", Long.class)))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(Map.of(), userDetails);
	}

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("JWT.SECRET_KEY"));
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private boolean isTokenExpired(String token) {
		return extractExpired(token).before(new Date());
	}

	private Date extractExpired(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// 토큰의 UUID 를 get, 그리고 토큰에 정보가 있는지 체크
	public String validateAndGetUserUuid(String token) {
		try {
			return extractClaim(token, Claims::getSubject);
		} catch (NullPointerException e) {
			log.info("토큰에 담긴 정보가 없음");
			return null;
		}
	}

	// 토큰에서 UUID 추출
	public String getUuid(String token) {
		return extractClaim(token, Claims::getSubject);
	}
}




