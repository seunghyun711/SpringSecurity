package com.example.SpringSecurityPractice.auth;

import com.example.SpringSecurityPractice.auth.utils.HelloAuthorityUtils;
import com.example.SpringSecurityPractice.exception.BusinessLogicException;
import com.example.SpringSecurityPractice.exception.ExceptionCode;
import com.example.SpringSecurityPractice.member.Member;
import com.example.SpringSecurityPractice.member.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Optional;

public class HelloUserDetailsService implements UserDetailsService { // Custom UserDetailsService 구현 위해 UserDetailsService 인터페이스를 구현
    private final MemberRepository memberRepository;
    private final HelloAuthorityUtils authorityUtils;


    public HelloUserDetailsService(MemberRepository memberRepository, HelloAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.authorityUtils = authorityUtils;
    }

    @Override
    // UserDetailService를 implements 하는 구현 클래스는 아래 메서드를 구현해야 한다.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        // HelloAuthrorityUtils를 이용해 db에서 조회한 회원의 이메일 정보를 이용해 ROle 기반의 권한 정보 컬렉션 생성
//        Collection<? extends GrantedAuthority> authorities = authorityUtils.createAuthorities(findMember.getEmail()); -> HelloUserDetails클래스 내부로 포함된다.
        /*
        db에서 조회한 인증 정보와 위에서 생성한 권한 정보를 스프링 시큐리티에서는 아직 알지 못하기 때문에 스프링 시큐리티에 이 정보들을 제공해야 하고,
        아래 코드에서는 UserDetails 인터페이스의 구현체인 User 클래스의 객체를 통해 제공한다.
         */

        // 데이터베이스에서 조회한 User 클래스의 객체를 리턴하면 스프링 시큐리티가 이 정보를 이용해 인증 절차를 수행한다.
        return new HelloUserDetails(findMember); // Custom UserDetails 클래스의 생성자로 findMember 전달

        // db에서 User의 인증 정보만 스프링 시큐리티에 넘기고, 인증 처리는 스프링 시큐리티가 대신한다.

        /*
         UserDetails는 UserDetailsService에 의해 로드되어 인증을 위해 사용되는 핵심 User 정보를 표현하는 인터페이스.
         UserDetails 인터페이스의 구현체는 스프링 시큐리티에서 보안 정보 제공을 목적으로 직접 사용되지는 않고, Authentication 객체로 캡슐화되어 사용된다.
         */
    }

    // HelloUserDetails 클래스 추가 : Member 엔티티 클래스 상속하고 있기 때문에 HelloUserDetails를 리턴 받아 사용하는 측에서 두 개 클래스의 객체를 모두 쉽게 캐스팅해서 사용 가능
    /*
    데이터베이스에서 조회한 회원 정보를 스프링 시큐리티의 User 정보로 변환하는 과정과 User의 권한 정보를 생성하는 과정을 캡슐화한다.
     */
    private final class HelloUserDetails extends Member implements UserDetails{ // 2-1
        //2-2
        HelloUserDetails(Member member) {
            setMemberId(member.getMemberId());
            setFullName(member.getFullName());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
            setRoles(member.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            // HellpAuthorityUtils의 createAuthorities() 메서드를 이용해 User의 권한 정보를 생성
            return authorityUtils.createAuthorities(this.getRoles());
        }

        // spring security에서 인식할 수 있는 username을 Member클래스의 email주소로 채운디.
        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
