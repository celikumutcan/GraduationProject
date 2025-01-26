package gp.graduationproject.summer_internship_back.infrastructure.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String SECRET_KEY = System.getenv("JWT_SECRET");  // Retrieve the key from environment variables

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // Extract the Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove "Bearer " prefix

            try {
                SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
                // Parse and validate the token
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                // Extract username and other claims (if needed)
                String username = claims.getSubject();

                if (username != null) {
                    // Create an authenticated token and set it in the security context
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Handle token validation errors (e.g., expired, invalid)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Invalid or expired token\"}");
                return; // Do not proceed with the request if the token is invalid
            }
        }

        // Continue with the filter chain if the token is valid or not present
        filterChain.doFilter(request, response);
    }
}
