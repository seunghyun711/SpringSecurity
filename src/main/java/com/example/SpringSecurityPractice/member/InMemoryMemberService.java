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

public class InMemoryMemberService implements MemberService {
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public InMemoryMemberService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        // UserDetailsManager는 스프링 시큐리티의 유저를 관리하는 관리자 역할. UserDetailsManager인터페이스의 하위 타입은 InMemoryMemberUserDetailsManager다.
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder; // spring security user 등록 시 패스워드를 암호화 해주는 클래스
    }


    public Member createMember(Member member) {
        // User의 권한 목록을 List<GrantedAuthority>로 생성
        // Role 베이스 형태의 권한 지정시 반드시 ROLE_ + 권한명의 형태로 지정해야 한다.
        List<GrantedAuthority> authorities = createAuthorities(Member.MemberRole.ROLE_USER.name());

        // user의 패스워드 암호화
        String encryptedPassword = passwordEncoder.encode(member.getPassword());

        // Spring Security user로 등록하기 위해 UserDetails 생성 -> 스프링 시큐리티에서 관리하는 user 정보는 UserDetails로 관리
        UserDetails userDetails = new User(member.getEmail(), encryptedPassword, authorities);

        // User 등록
        userDetailsManager.createUser(userDetails);

        return member;
    }

    private List<GrantedAuthority> createAuthorities(String... roles) {
        return Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }
}
