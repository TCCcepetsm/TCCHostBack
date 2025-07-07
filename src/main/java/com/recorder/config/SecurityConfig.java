package com.recorder.config;

import com.recorder.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final String[] PUBLIC_ENDPOINTS = {
			"/api/auth/**",
			"/api/usuarios/register",
			"/api/usuarios/login",
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/api-docs/**",
			"/api/agendamentos2"
	};

	private static final String[] AUTHENTICATED_ENDPOINTS = {
			"/api/auth/validate-token",
			"/api/agendamentos2/criar2"
	};

	private static final String[] ADMIN_ENDPOINTS = {
			"/api/admin/**"
	};

	private static final String[] PROFISSIONAL_ENDPOINTS = {
			"/api/profissional/**"
	};

	private static final String[] AGENDAMENTO_ENDPOINTS = {
			"/api/agendamentos/**"
	};

	private final JwtAuthFilter jwtAuthFilter;

	public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors().configurationSource(corsConfigurationSource())
				.and()
				.csrf().disable()
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
						.requestMatchers(AUTHENTICATED_ENDPOINTS).authenticated()
						.requestMatchers(ADMIN_ENDPOINTS).hasAuthority("ROLE_ADMIN")
						.requestMatchers(PROFISSIONAL_ENDPOINTS).hasAuthority("ROLE_PROFISSIONAL")
						.requestMatchers(AGENDAMENTO_ENDPOINTS)
						.hasAnyAuthority("ROLE_USUARIO", "ROLE_PROFISSIONAL", "ROLE_ADMIN")
						.anyRequest().authenticated())
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		// Configuração para Render.com e desenvolvimento local
		config.setAllowedOriginPatterns(List.of(
				"https://tcchostfront.onrender.com",
				"http://localhost:*",
				"http://127.0.0.1:*"));

		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		config.setExposedHeaders(List.of("Authorization"));
		config.setAllowCredentials(true);
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}