package com.example.SpringSecurityPractice.config;

import com.example.SpringSecurityPractice.member.InMemoryMemberService;
import com.example.SpringSecurityPractice.member.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaConfiguration {
    @Bean
    public MemberService inMemoryMemberService() {
        return new InMemoryMemberService();
    }
}
