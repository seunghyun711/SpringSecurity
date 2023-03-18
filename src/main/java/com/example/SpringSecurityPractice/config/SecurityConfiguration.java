package com.example.SpringSecurityPractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class SecurityConfiguration { // 여기에 Spring Security에서 지원하는 인증과 권한 부여 설정을 한다.
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
