package com.example.SpringSecurityPractice.member;

import com.example.SpringSecurityPractice.auth.utils.HelloAuthorityUtils;
import com.example.SpringSecurityPractice.exception.BusinessLogicException;
import com.example.SpringSecurityPractice.exception.ExceptionCode;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class DBMemberServiceV2 {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final HelloAuthorityUtils authorityUtils;

    public DBMemberServiceV2(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
                             HelloAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
    }

    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());
        String encryptedPassword = passwordEncoder.encode(member.getPassword()); // 패스워드 암호화
        member.setPassword(encryptedPassword); // 암호화된 패스워드를 password필드에 다시 할당

        // role 을 db에 저장
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }
}
