package com.example.SpringSecurityPractice.filter;

import javax.servlet.*;
import java.io.IOException;

public class FirstFilter implements Filter {
    // 초기화
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("FirstFilter 생성됨");
    }

    // filter 가 처리하는 실질적인 로직
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // request(ServletRequest)를 이용해 다음 Filter로 넘어가기 전 전처리 작업 수행
        System.out.println("======= First 필터 시작 =======");
        chain.doFilter(request, response);
        // response(ServletRequest)를 이용해 response에 대한 후처리 작업
        System.out.println("======= First 핉터 종료 =======");

    }

    // filter가 컨테이너에서 종료될 때 호출되는데 주로 filter가 사용한 자원을 반납하는 처리 등의 로직 구현
    @Override
    public void destroy() {
        // filter가 사용한 자원을 반납하는 처리
        System.out.println("FirstFilter Destroy");
        Filter.super.destroy();
    }
}
