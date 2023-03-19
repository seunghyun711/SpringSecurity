package com.example.SpringSecurityPractice.auth;

import com.example.SpringSecurityPractice.auth.utils.HelloAuthorityUtils;
import com.example.SpringSecurityPractice.exception.BusinessLogicException;
import com.example.SpringSecurityPractice.exception.ExceptionCode;
import com.example.SpringSecurityPractice.member.Member;
import com.example.SpringSecurityPractice.member.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
@Component
public class HelloUserDetailsServiceV1 implements UserDetailsService{// Custom UserDetailsService 구현 위해 UserDetailsService인터페이스를 구현한다.
    private final MemberRepository memberRepository;
    private final HelloAuthorityUtils authorityUtils;

    // 데이터베이스에서 User를 조회하고 조회한 User의 권한 정보를 생성하기 위해 di
    public HelloUserDetailsServiceV1(MemberRepository memberRepository, HelloAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.authorityUtils = authorityUtils;
    }

    // UserDetailsService 인터페이슬르 implements하는 구현 클래스는 loadUserByUsername()이라는 추상 메서드 구현해야 한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        // HelloAuthorityUtils를 이용해 데이터베이스에서 조회한 회원의 이메일 정보를 이용해 Role 기반의 권한 정보 컬렉션 생성
        Collection<? extends GrantedAuthority> authorities = authorityUtils.createAuthorities(findMember.getEmail());

        // 데이터베이스에서 조회한 User 클래스의 객체를 리턴하면 스프링 시큐리티가 이 정보를 이용해 인증 절차 수행
        // 데이터베이스에서 User의 인증 정보만 스프링 시큐리티에 넘기고 인증 처리는 스프링 시큐리티가 대신한다.
        return new User(findMember.getEmail(), findMember.getPassword(), authorities);
    }
}
