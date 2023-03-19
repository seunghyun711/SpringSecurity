package com.example.SpringSecurityPractice.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class HelloUserAuthenticationProvider implements AuthenticationProvider {
    private final HelloUserDetailsServiceV1 userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public HelloUserAuthenticationProvider(HelloUserDetailsServiceV1 userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    // 3
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // authentication을 캐스팅하여 UsernamePasswordAuthenticationToken을 얻는다.
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;

        // 해당 사용자의 Username이 존재하는지 체크
        String username = authToken.getName();
        Optional.ofNullable(username).orElseThrow(() ->
                new UsernameNotFoundException("Invalid User name or User Password"));

        // username 존재시 userDetailsService를 이용해 데이터베이스에서 해당 사용자 조회
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String password = userDetails.getPassword();
        verifyCredentials(authToken.getCredentials(), password); // 로그인 정보에 포함된 패스워드와 db에 저장된 사용자의 패스워드가 일치하는지 확인

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities(); // 패스워드 일치하면 사용자 권한 생성

        // 인증된 사용자의 인증 정보를 리턴값으로 전달
        return UsernamePasswordAuthenticationToken.authenticated(username, password, authorities);
    }

    // 2 HelloUserAuthenticationProvider가 Username/Password 방식의 인증을 지원한단느 것을 스프링 시큐리티에 알림
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

    public void verifyCredentials(Object credentials, String password) {
        if (!passwordEncoder.matches((String) credentials, password)) {
            throw new BadCredentialsException("Invalid User name or User Password");
        }
    }
}
