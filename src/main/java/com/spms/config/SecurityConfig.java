package com.spms.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	 @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	 
	 @Bean
	 public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
	     org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
	     config.setAllowedOriginPatterns(java.util.List.of("*"));
	     config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	     config.setAllowedHeaders(java.util.List.of("*"));
	     config.setAllowCredentials(true);

	     org.springframework.web.cors.UrlBasedCorsConfigurationSource source =
	         new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
	     source.registerCorsConfiguration("/**", config);
	     return source;
	 }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> {})
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/public/**").permitAll()
            .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/students/**").hasRole("ADMIN")
            .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/students/**").hasRole("ADMIN")
            .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/students/**").hasRole("ADMIN")
            .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/grades/**").hasAnyRole("ADMIN", "TEACHER")
            .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/**").authenticated()
            .anyRequest().authenticated()
        )
        .httpBasic(basic -> {});
    return http.build();
    }
}
