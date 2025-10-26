package com.company.bus_mgmt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokens;

    public JwtAuthenticationFilter(JwtTokenProvider tokens) {
        this.tokens = tokens;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                Jws<io.jsonwebtoken.Claims> jws = tokens.parse(token);
                Claims c = jws.getBody();
                String sub = c.getSubject();
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) c.get("roles");
                Collection<SimpleGrantedAuthority> auths = roles == null ? List.of()
                        : roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).toList();
                AbstractAuthenticationToken at = new AbstractAuthenticationToken(auths) {
                    @Override public Object getCredentials() { return token; }
                    @Override public Object getPrincipal() { return sub; }
                };
                at.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(at);
            } catch (Exception ex) {
                System.err.println("[JWT] Failed to parse/validate token for " + req.getMethod() + " " + req.getRequestURI()
                        + " : " + ex.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(req, res);
    }
}
