package com.example.SpringSecurityPractice.member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// InMemory User 등록을 위한 클래스
public class InMemoryMemberService implements MemberService { // MemberService를 구현하는 클래스
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    /*
    UserDetailsManager : 스프링 시큐리티의 User를 관리하는 관리자 역할 SecurityConfiguration에서 빈으로 등록한 UserDetailsManager는
    InMemoryUserDetailsManager이므로 여기서 DI 받은 UserDetailsManager의 하위 타입은 InMemoryUserDetailsMamger디.

    PasswordEncoder : 스프링 시큐리티 User를 등록할 때 패스워드를 암호화 하는 클래스
     */
    public InMemoryMemberService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public Member createMember(Member member) {
        /*
         createAuthorities()를 통해 User의 권한 목록을 List<GrangtedAuthority>로 생성한다.
         스프링 시큐리티에서는 SimpleGrantedAuthority를 사용해 Role 베이스 형태의 권한을 설정할 때 "ROLE_" + 권한 의 형태로 지정해야 한다.
         */
        List<GrantedAuthority> authorities = createAuthorities(Member.MemberRole.ROLE_USER.name());

        // PasswordEncoder를 이용해 등록할 User의 패스워드를 암호화한다.(User 등록시 암호화 필수)
        String encryptedPassword = passwordEncoder.encode(member.getPassword());

        // 스프링 시큐리티 User로 등록하기 위해 UserDetails 생성
        UserDetails userDetails = new User(member.getEmail(), encryptedPassword, authorities);

        // UserDetailsManager의 createUser()를 이용해 User 등록
        userDetailsManager.createUser(userDetails);

        return member;
    }

    private List<GrantedAuthority> createAuthorities(String... roles) {
        // stream으로 생성자 파라미터로 해당 User의 Role을 전달하여 SimpleGrantedAuthority 객체를 생성하고 List<GrantedAuthority> 형태로 리턴
        return Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }
}
