package com.example.SpringSecurityPractice.config;

import com.example.SpringSecurityPractice.member.DBMemberService;
import com.example.SpringSecurityPractice.member.InMemoryMemberService;
import com.example.SpringSecurityPractice.member.MemberRepository;
import com.example.SpringSecurityPractice.member.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class JavaConfiguration {
//    @Bean
//    public MemberService inMemoryMemberService(UserDetailsManager userDetailsManager,
//                                               PasswordEncoder passwordEncoder) {
//        return new InMemoryMemberService(userDetailsManager, passwordEncoder);
//    }

    @Bean // 데이터베이스에 User 정보 저장하기 위해 MemberService 인터페이스의 구현 클래스를 DBMemberService로 변경
    public MemberService dbMemberService(MemberRepository memberRepository,
                                         PasswordEncoder passwordEncoder) {
        return new DBMemberService(memberRepository, passwordEncoder);
    }
}
