package com.recorder.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

	private static final String ROLE_PREFIX = "ROLE_";
	private static final String ROLE_CLAIM = "role";
	private static final String AUTHORITIES_CLAIM = "authorities";

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long jwtExpirationMs; // Mudado para long diretamente

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public String extractRole(String token) {
		return extractClaim(token, claims -> {
			// Tenta obter a role do claim específico
			String role = claims.get(ROLE_CLAIM, String.class);
			if (role != null) {
				return role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role;
			}

			// Fallback para authorities (compatibilidade)
			if (claims.containsKey(AUTHORITIES_CLAIM)) {
				return claims.get(AUTHORITIES_CLAIM, String.class)
						.lines()
						.filter(auth -> auth.contains(ROLE_PREFIX))
						.findFirst()
						.map(auth -> auth.split(ROLE_PREFIX)[1].replaceAll("\"", ""))
						.orElse(null);
			}

			return null;
		});
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		String mainRole = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.filter(auth -> auth.startsWith(ROLE_PREFIX))
				.findFirst()
				.orElse(ROLE_PREFIX + "USER");

		Map<String, Object> claims = new HashMap<>(extraClaims);
		claims.put(ROLE_CLAIM, mainRole);
		claims.put(AUTHORITIES_CLAIM, userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList()));

		return buildToken(claims, userDetails.getUsername());
	}

	private String buildToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}