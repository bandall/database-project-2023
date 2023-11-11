package com.ajousw.spring.domain.member.repository;

import com.ajousw.spring.domain.alarm.repository.Alarm;
import com.ajousw.spring.domain.member.enums.LoginType;
import com.ajousw.spring.domain.member.enums.Role;
import com.ajousw.spring.domain.timetable.repository.TimeTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private TimeTable timeTable;

    @OneToMany(mappedBy = "member")
    private List<Alarm> alarmList = new ArrayList<>();

}
