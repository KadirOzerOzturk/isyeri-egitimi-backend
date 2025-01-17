package com.isyeriegitimi.backend.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
        final String jwt;
        final String username;

        try {
            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = header.substring(7);
            username = jwtService.findUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.tokenControl(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            handleException(response, "JWT expired. Please log in again.", 401);
        } catch (Exception ex) {
            handleException(response, "Authentication failed: " + ex.getMessage(), 500);
        }
    }

    private void handleException(HttpServletResponse response, String message, int status) throws IOException {
        ApiResponse<Object> apiResponse = ApiResponse.error(message, status);
        response.setStatus(status);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
    }
}
