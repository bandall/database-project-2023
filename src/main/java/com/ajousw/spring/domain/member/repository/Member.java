package com.ajousw.spring.domain.member.repository;

import com.ajousw.spring.domain.member.enums.LoginType;
import com.ajousw.spring.domain.member.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(length = 50)
    private String username;

    @Column(length = 255)
    private String profileImageUri;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime lastLoginTime;

//    @Builder
//    public Member(String email, String username, Role role) {
//        this.email = email;
//        this.username = username;
//        this.role = role;
//    }

    public void updateLastLoginTime() {
        this.lastLoginTime = LocalDateTime.now();
    }
}
