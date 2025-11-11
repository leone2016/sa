package com.example.contact;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults());
		http
			.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
		http
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		);
		return http.build();
	}
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(jwt -> {
			Set<String> roles = new HashSet<>();
			// Realm roles
			Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
			if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?> realmRoles) {
				roles.addAll(realmRoles.stream().map(Object::toString).toList());
			}
			// Client roles (replace "myclient" with your client ID)
			Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
			if (resourceAccess != null && resourceAccess.get("myclient") instanceof Map<?, ?> clientAccess) {
				Object clientRoles = ((Map<?, ?>) clientAccess).get("roles");
				if (clientRoles instanceof Collection<?> clientRolesList) {
					roles.addAll(clientRolesList.stream().map(Object::toString).toList());
				}
			}
			System.out.println("Roles extracted: " + roles);
			// Convert to SimpleGrantedAuthority with ROLE_ prefix
			return roles.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
				.collect(Collectors.toSet());
		});
		return converter;
	}
}