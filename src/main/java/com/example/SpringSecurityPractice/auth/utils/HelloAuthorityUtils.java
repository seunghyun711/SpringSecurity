package com.example.SpringSecurityPractice.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HelloAuthorityUtils {
    // application.yml에 추가한 프로퍼티를 가져오는 표현식
    // @Value 애너테이션으로 아래와 같이 작성하면 application.yml에 정의되어 있는 프로퍼티의 값을 클래스 내에서 사용할 수 있다.
    @Value("${mail.address.admin}") // yml에 정의한 관리자용 이메일 주소는 회원 등록 시 특정 이메일 주소에 관리자 권한을 부여할 수 있는지를 결정하기 위해 사용한다.
    private String adminMailAddress;

    // Sppring Security에서 지원하는 AuthorityUtils 클래스를 이용해 관리자용 권한 목록을 List<GrantedAuthority> 객체로 미리 생성
    private final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");

    // 스프링 시큐리티에서 지원하는 AuthorityUtils 클래스를 이용해 일반 사용자 권한 목록을 List<GrantedAuthority> 객체로 미리 생성
    private final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");

    private final List<String> ADMIN_ROLES_STRING = List.of("ADMIN", "USER");
    private final List<String> USER_ROLES_STRING = List.of("USER");

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

    // DB 저장용
    /*
    파라미터로 전달된 이메일 주소가 application.yml 파일의 mail.address.admin 프로퍼티에 정의된 이메일 주소와 동일하면
    관리자 Role 목록(adMin_ROLES_STRING)을 리턴하고 그외에는 일반 사용자 ROle 목록(USER_ROLES_STRING) 리턴
     */
    public List<String> createRoles(String email) {
        if (email.equals(adminMailAddress)) {
            return ADMIN_ROLES_STRING;
        }
        return USER_ROLES_STRING;
    }

    // 1 db에 저장된 Role을 기반으로 권한 정보 생성
    public List<GrantedAuthority> createAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // 2
                .collect(Collectors.toList());
        return authorities;
    }

}
