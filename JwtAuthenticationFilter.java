package com.app.vote.security;

import com.app.vote.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@Setter(onMethod = @__(@Autowired))
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> tokenOptional = Optional.ofNullable(request.getHeader("Authorization"))
                    .map(str -> str.substring(7))
                    .filter(StringUtils::isNotBlank);

            if (tokenOptional.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
                Jws<Claims> claimsJws = tokenService.verify(tokenOptional.get());
                UsernamePasswordAuthenticationToken authentication = tokenService.createAuthenticateToken(claimsJws);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("JWT authentication filter error", e);
        }
        filterChain.doFilter(request, response);
    }
}
