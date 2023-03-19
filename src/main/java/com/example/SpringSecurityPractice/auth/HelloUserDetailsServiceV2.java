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

// HelloUserDetailsServiceV1 에서 개선된 버전
public class HelloUserDetailsServiceV2 implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final HelloAuthorityUtils authorityUtils;

    public HelloUserDetailsServiceV2(MemberRepository memberRepository, HelloAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.authorityUtils = authorityUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return new HelloUserDetails(findMember); // 개선된 부분 -> Custom UserDetails 클래스의 생성자로 findMember를 전달하여 코드가 간결해짐
    }

    // HelloUserDetails 클래스 추가
    /*
    데이터베이스에서 조회한 회원 정보를 스프링 시큐리티의 User 정보로 변환하는 과정과 User의 권한 정보를 생성한는 과정을 캡슐화할 수 있다.
    HelloUserDetails 클래스는 Member 엔티티 클래스를 상속하고 있기 때문에 HelloUserDetails를 리턴 받아 사용하는 측에서는 두 개 클래스의
    객체를 모두 쉽게 캐스팅 하여 사용 가능하다.
     */
    private final class HelloUserDetails extends Member implements UserDetails {
        // 2-2
        HelloUserDetails(Member member) {
            setMemberId(member.getMemberId());
            setFullName(member.getFullName());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getEmail()); // User 권한 정보 생성
        }

        // 스프링 시큐리티에서 인식할 수 있는 username을 Member 클래스의 email 주소로 채우고 있다.
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
