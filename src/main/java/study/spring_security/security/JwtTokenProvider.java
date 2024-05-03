package study.spring_security.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

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

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("JWT.SECRET"));
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

}




