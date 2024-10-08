package com.online_store.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online_store.security.auth.service.JwtService;
import com.online_store.utils.MessageResponse;
import com.online_store.utils.constant.ErrorMessage;
import com.online_store.web.user.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(authHeader)) {
            filterChain.doFilter(request, response);
            log.warn("Authorization header is missing");
            return;
        }
        if (!authHeader.startsWith(BEARER_PREFIX)) {
            MessageResponse error = new MessageResponse(String.valueOf(HttpStatus.FORBIDDEN), ErrorMessage.MISS_BEARER_PREFIX);
            sendErrorResponse(response, error, HttpStatus.FORBIDDEN);
            log.warn(error.getMessage());
            return;
        }

        String jwt = authHeader.substring(BEARER_PREFIX.length());
        try {
            String email = jwtService.extractEmailFromToken(jwt);
            if (StringUtils.hasText(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(email);
                if (jwtService.validateJwtToken(jwt, userDetails)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, new MessageResponse(String.valueOf(HttpStatus.UNAUTHORIZED), ErrorMessage.TOKEN_EXPIRED), HttpStatus.UNAUTHORIZED);
            log.warn(e.getMessage());
        } catch (JwtException e) {
            sendErrorResponse(response, new MessageResponse(String.valueOf(HttpStatus.FORBIDDEN), ErrorMessage.TOKEN_INVALID), HttpStatus.FORBIDDEN);
            log.warn(e.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletResponse response, MessageResponse message, HttpStatus status
    ) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(message));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
