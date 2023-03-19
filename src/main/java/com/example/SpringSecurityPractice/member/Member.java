package com.example.SpringSecurityPractice.member;

import com.example.SpringSecurityPractice.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member extends Auditable implements Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(length = 100, nullable = false)
    private String fullName;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    // Member 엔티티에 User 권한 정보 매핑
    @ElementCollection(fetch = FetchType.EAGER) // User 권한 정보와 관련된 별도의 엔티티 클래스를 생성하지 않아도 간단하게 매핑 처리 된다.
    private List<String> roles = new ArrayList<>();

    public Member(String email) {
        this.email = email;
    }

    public Member(String email, String fullName, String password) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    @Override
    public String getName() {
        return getEmail();
    }

    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        @Getter
        private String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }

    public enum MemberRole {
        ROLE_USER,
        ROLE_ADMIN
    }
}
