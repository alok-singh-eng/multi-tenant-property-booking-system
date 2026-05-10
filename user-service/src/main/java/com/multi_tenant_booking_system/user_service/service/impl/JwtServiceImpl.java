package com.multi_tenant_booking_system.user_service.service.impl;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.multi_tenant_booking_system.user_service.entity.User;
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
	public String extractUsername(String token) {
		try {
			return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload().getSubject();
		}
		catch (ExpiredJwtException ex) {
			log.debug("JWT expired: {}", ex.getMessage());
			return null;
		}
		catch (JwtException | IllegalArgumentException ex) {
			log.debug("Invalid JWT: {}", ex.getMessage());
			return null;
		}
	}
}
