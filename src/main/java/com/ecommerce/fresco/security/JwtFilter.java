package com.ecommerce.fresco.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	CustomUserdetailsService userdetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String bearerToken = request.getHeader("JWT");

		if (bearerToken != null) {

			String token = bearerToken;

			String userName = jwtUtil.getUserNameFromToken(token);
			
			System.out.println(SecurityContextHolder.getContext().getAuthentication());

			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userdetailsService.loadUserByUsername(userName);

				if (jwtUtil.validateToken(userDetails, token)) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userName, userDetails,userDetails.getAuthorities());

					
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					
					System.out.println(SecurityContextHolder.getContext().getAuthentication());

				}

			}

		}
		
		filterChain.doFilter(request, response);

	}

}
