package com.online_store.security.auth;

import com.online_store.utils.constant.ErrorMessage;
import com.online_store.web.user.model.User;
import com.online_store.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userService.findUserByEmail(email);

        // authenticate user
        if (user == null || !email.equals(user.getEmail()))
            throw new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND);

        // check password
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException(ErrorMessage.PASSWORDS_DO_NOT_MATCH);

        // check if user is active
        if (!user.getActive())
            throw new DisabledException(ErrorMessage.USER_NOT_ACTIVE);

        // get user roles
        String userRole = user.getRoles().iterator().next().toString();
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userRole));
        return new UsernamePasswordAuthenticationToken(email, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
