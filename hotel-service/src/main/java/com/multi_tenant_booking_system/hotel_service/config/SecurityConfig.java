package com.multi_tenant_booking_system.hotel_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.multi_tenant_booking_system.hotel_service.utility.ApiPath;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(HttpMethod.GET, ApiPath.HOTEL_BASE + ApiPath.HEALTH).permitAll()
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            .requestMatchers("/error").permitAll()
            .requestMatchers(HttpMethod.GET, ApiPath.HOTEL_BASE).permitAll()
            .requestMatchers(HttpMethod.GET, ApiPath.HOTEL_BASE + "/**").permitAll()
            .requestMatchers(HttpMethod.POST, ApiPath.HOTEL_BASE + "/*/reviews").hasRole("USER")
            .requestMatchers(HttpMethod.POST, ApiPath.HOTEL_BASE).hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, ApiPath.HOTEL_BASE + "/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, ApiPath.HOTEL_BASE + "/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, ApiPath.HOTEL_BASE + "/**").hasRole("ADMIN")
            .anyRequest().denyAll())
        .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler))
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
