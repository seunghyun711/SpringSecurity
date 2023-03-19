package com.example.SpringSecurityPractice.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HelloAuthorityUtils {
    // application.yml에 추가한 프로퍼티를 가져오는 표현식
    // @Value 애너테이션으로 아래와 같이 작성하면 application.yml에 정의되어 있는 프로퍼티의 값을 클래스 내에서 사용할 수 있다.
    @Value("${mail.address.admin") // yml에 정의한 관리자용 이메일 주소는 회원 등록 시 특정 이메일 주소에 관리자 권한을 부여할 수 있는지를 결정하기 위해 사용한다.
    private String adminMailAddress;

    // Sppring Security에서 지원하는 AuthorityUtils 클래스를 이용해 관리자용 권한 목록을 List<GrantedAuthority> 객체로 미리 생성
    private final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");

    // 스프링 시큐리티에서 지원하는 AuthorityUtils 클래스를 이용해 일반 사용자 권한 목록을 List<GrantedAuthority> 객체로 미리 생성
    private final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");

    public List<GrantedAuthority> createAuthorities(String email) {
        /*
         파라미터로 전달받은 이메일 주소가 application.yml 파일에서 가져온 관리자용 이메일 주소와 동일하다면 관리자용 권한인
         List<GrantedAuthority> ADMIN_ROLES를 리턴한다.
         */
        if (email.equals(adminMailAddress)) {
            return ADMIN_ROLES;
        }
        return USER_ROLES;
    }

}
