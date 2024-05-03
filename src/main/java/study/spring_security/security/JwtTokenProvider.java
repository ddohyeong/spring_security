package study.spring_security.security;

import java.security.Key;
import java.util.Map;
import java.util.Objects;

import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;

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

}




