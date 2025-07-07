package com.recorder.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

	@Value("${application.security.jwt.secret-key}")
	private String secretKey;

	@Value("${application.security.jwt.expiration}")
	private long jwtExpiration;

	@Value("${application.security.jwt.refresh-token.expiration}")
	private long refreshExpiration;

	// Métodos para extrair informações do token
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Método principal para extrair todas as claims do token
	private Claims extractAllClaims(String token) {
		// ESSA É A LINHA CRÍTICA PARA O parserBuilder()
		return Jwts.parserBuilder()
				.setSigningKey(getSignInKey()) // Depende de getSignInKey()
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	// MÉTODO ESSENCIAL: Gerar a chave de assinatura
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// Métodos para gerar o token
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails) {
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	// Método auxiliar para construir o token
	private String buildToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails,
			long expiration) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey(), io.jsonwebtoken.SignatureAlgorithm.HS256)
				.compact();
	}

	// Métodos para validação do token
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// MÉTODO: extractRole(String token) - Este é o que estava faltando ou inválido
	// para o JwtAuthFilter
	// Adapte este método com base em como você armazena os papéis (roles) no seu
	// token.
	public String extractRole(String token) {
		Claims claims = extractAllClaims(token);
		// Exemplo comum: A role é armazenada em uma claim customizada chamada "roles"
		// ou "authorities"
		// Se for uma String:
		if (claims.containsKey("authorities")) {
			return claims.get("authorities", String.class);
		}
		// Se for uma lista de Strings (e você quer a primeira):
		// if (claims.containsKey("roles")) {
		// List<String> roles = claims.get("roles", List.class);
		// if (roles != null && !roles.isEmpty()) {
		// return roles.get(0);
		// }
		// }
		// Fallback: se não encontrar uma claim específica, talvez o subject (username)
		// sirva como role
		return claims.getSubject(); // Ou retorne um valor padrão como "USUARIO" ou null
	}
}