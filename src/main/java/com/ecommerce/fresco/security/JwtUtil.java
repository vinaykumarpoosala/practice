package com.ecommerce.fresco.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	
	private String secret = "secretkey";

	
	private Long validity = 90000L;

	public String generateToken(UserDetails userdetails) {

		Map<String, Object> claims = new HashMap<>();
		return createToken(userdetails, claims);
	}

	private String createToken(UserDetails userdetails, Map<String, Object> claims) {
		// TODO Auto-generated method stub
		return Jwts.builder().setClaims(claims).setSubject(userdetails.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + validity * 60 * 1000))

				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	public String getUserNameFromToken(String token) {

		return extractClaim(Claims::getSubject, token);
	}

	public <T> T extractClaim(Function<Claims, T> claimResolver, String token) {

		final Claims claims = getallClaims(token);

		return claimResolver.apply(claims);
	}

	public Claims getallClaims(String token) {

		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

	}

	public Boolean isExpired(String token) {
		
		return getExpiration(token).before(new Date());
	}

	public Date getExpiration(String token) {

		return extractClaim(Claims::getExpiration, token);

	}

	public boolean validateToken(UserDetails userDetails, String token) {
		// TODO Auto-generated method stub
		
		return getUserNameFromToken(token).equals(userDetails.getUsername()) && !isExpired(token);
		
	}
}
