package com.example.SpringSecurityPractice.filter;

import javax.servlet.*;
import java.io.IOException;

public class SecondFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("SecondFilter가 생성되었습니다.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("======= Second 필터 시작 ========");
        chain.doFilter(request, response);
        System.out.println("======= Second 필터 종료 =======");
    }

    @Override
    public void destroy() {
        System.out.println("Second Filter가 사라집니다.");
        Filter.super.destroy();
    }
}
