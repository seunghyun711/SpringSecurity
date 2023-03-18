package com.example.SpringSecurityPractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration { // 여기에 Spring Security에서 지원하는 인증과 권한 부여 설정을 한다.

    @Bean
    // http 파라미터를 가지고 SecurityFilterChain을 리턴하는 형태의 메서드를 정의하면 http 보안 설정을 구성할 수 있다.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 공격에 대한 스프링 시큐리티에 대한 설정 비활성화 -> 현재 로컬에서 진행하기 때문에 CSRF 공격에 대한 설정 필요하지 않다.
                .formLogin() // 기본 적인 인증 방법을 폼 로그인 방식으로 지정
                .loginPage("/auths/login-form") // 템플릿 프로젝트에 미리 만든 커스텀 로그인 페이지를 사용하도록 지정
                .loginProcessingUrl("/process_login") // 로그인 인증 요청을 수행할 요청 URL 지정 -> login.html에서 form 태그의 action 속성에 지정한 URL과 동일
                .failureUrl("/auths/login-form?error") // 로그인 인증에 실패할 경우 리다이렉트할 화면 지정
                .and() // 스프링 시큐리티 보안 설정을 메서드 체인 형태로 구성
                .authorizeHttpRequests() // 클라이언트의 요청일 들어오면 접근 권한을 확인하기 위해 정의
                // 클라이언트의 모든 요청에 대한 접근 허용
                .anyRequest()
                .permitAll();

        return http.build();
    }
    /*
    아래 방식은 사용자의 계정 정보를 설정하고 고정 시키는 방식이다.
    실제 서비스에서는 이렇게 사용하지 않고, 테스트 환경이나 데모 환경에서 유용하게 사용할 수 있는 방식이다.
     */
    @Bean
    public UserDetailsManager userDetailsManager(){
        UserDetails userDetails = // UserDetails는 인증된 사용자의 핵심정보를 포함한다.
                User.withDefaultPasswordEncoder() // WithDefaultPasswordEncorder()는 디폴트 패스워드 인코더를 이용해 사용자 패스워드 암호화한다.
                        .username("hong@never.com") // 사용자의 username설정
                        .password("1111") // 사용자의 password 설정
                        .roles("USER") // 사용자의 역할 설정
                        .build();

        /*
        메모리상에서 UserDetails를 관리하므로 InMemoryUserDetailsManager라는 구현체 사용
         */
        return new InMemoryUserDetailsManager(userDetails);
    }

}
