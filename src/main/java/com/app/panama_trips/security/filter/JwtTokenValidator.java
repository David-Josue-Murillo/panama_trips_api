package com.app.panama_trips.security.filter;

import com.app.panama_trips.utility.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collection;

public class JwtTokenValidator extends OncePerRequestFilter {

    private JwtUtil jwtUtil;

    public JwtTokenValidator(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(jwtToken != null && jwtToken.startsWith("Bearer ")) {
            // Remove Bearer from the token to get the token itself
            jwtToken = jwtToken.substring(7);

            // Validate the token and get the username and authorities
            DecodedJWT decodedJWT = jwtUtil.validateToken(jwtToken);
            String username = jwtUtil.getUsernameFromToken(decodedJWT);
            String stringAuthorities = jwtUtil.getSpecificClaim(decodedJWT, "authorities").asString();

            // Create a collection of authorities
            Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

            // Create an authentication object
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }
}
