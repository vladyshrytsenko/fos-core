package com.example.foscore.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class FakeAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization-Test");

        if ("X-Authorization-test".equals(authHeader)) {
            Authentication authentication = new FakeAuthentication();
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    static class FakeAuthentication implements Authentication {
        private final Jwt jwt;

        public FakeAuthentication() {
            this.jwt = Jwt.withTokenValue("fake-token")
                .claim("user_id", 1L)
                .header("alg", "none")
                .build();
        }

        @Override
        public Object getPrincipal() {
            return jwt;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(() -> "ROLE_ADMIN");
        }

        @Override
        public Object getCredentials() { return null; }

        @Override
        public Object getDetails() { return null; }

        @Override
        public boolean isAuthenticated() { return true; }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}

        @Override
        public String getName() { return "fake-user"; }
    }
}
