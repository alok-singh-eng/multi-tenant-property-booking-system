package com.multi_tenant_booking_system.user_service.service.impl;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.multi_tenant_booking_system.user_service.dto.Role;
import com.multi_tenant_booking_system.user_service.entity.User;
import com.multi_tenant_booking_system.user_service.dto.JwtAuthenticationPrincipal;
import com.multi_tenant_booking_system.user_service.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

	private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration-minutes}")
	private long expirationTimeInMin;

	private SecretKey signingKey;

	@jakarta.annotation.PostConstruct
	void initKey() {
		byte[] keyBytes = secretKey == null ? new byte[0] : secretKey.getBytes(StandardCharsets.UTF_8);
		if (keyBytes.length < 32) {
			throw new IllegalStateException("jwt.secret must be at least 256 bits (32 bytes) for HS256.");
		}
		this.signingKey = Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public String generateToken(User user) {
		Instant now = Instant.now();
		Instant exp = now.plus(expirationTimeInMin, ChronoUnit.MINUTES);
		String roleClaim = user.getRole() != null ? user.getRole().name() : "";
		return Jwts.builder().subject(user.getEmail()).claim("role", roleClaim).issuedAt(Date.from(now))
				.expiration(Date.from(exp)).signWith(signingKey, Jwts.SIG.HS256).compact();
	}

	@Override
	public Optional<JwtAuthenticationPrincipal> parseAccessToken(String token) {
		try {
			var payload = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
			String email = payload.getSubject();
			String roleClaim = payload.get("role", String.class);
			Role role = parseRole(roleClaim);
			if (email == null || email.isBlank()) {
				return Optional.empty();
			}
			return Optional.of(new JwtAuthenticationPrincipal(email, role));
		}
		catch (ExpiredJwtException ex) {
			log.debug("JWT expired: {}", ex.getMessage());
			return Optional.empty();
		}
		catch (JwtException | IllegalArgumentException ex) {
			log.debug("Invalid JWT: {}", ex.getMessage());
			return Optional.empty();
		}
	}

	private static Role parseRole(String roleClaim) {
		if (roleClaim == null || roleClaim.isBlank()) {
			return Role.USER;
		}
		try {
			return Role.valueOf(roleClaim.trim());
		}
		catch (IllegalArgumentException ex) {
			return Role.USER;
		}
	}
}
