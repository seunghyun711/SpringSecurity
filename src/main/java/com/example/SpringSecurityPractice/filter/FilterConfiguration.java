package com.example.SpringSecurityPractice.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
Spring Boot에서 Servlet Filter는 FilterRegistrationBean의 생성자로 Filter 인터페이스의 구현 객체를 넘겨주는 형태로 등록 가능
 */
@Configuration
public class FilterConfiguration {
    @Bean
    public FilterRegistrationBean<FirstFilter> firstFilterRegister(){
        FilterRegistrationBean<FirstFilter> registrationBean = new FilterRegistrationBean<>(new FirstFilter());
        return registrationBean;
    }
}
