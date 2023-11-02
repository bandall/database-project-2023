package com.ajousw.spring.domain.alarm.repository;

import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.timetable.repository.Subject;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private Long day;

    private Long hour;

    private Long minute;

    private Long alarmGap;

    private Boolean isAlarmOn;

    @Builder
    public Alarm(Member member, Subject subject, Long day, Long hour, Long minute, Long alarmGap, Boolean isAlarmOn) {
        this.member = member;
        this.subject = subject;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.alarmGap = alarmGap;
        this.isAlarmOn = isAlarmOn;
    }

    public void setAlarmGap(Long alarmGap) {
        this.alarmGap = alarmGap;
    }

    public void setAlarmOn(Boolean alarmOn) {
        isAlarmOn = alarmOn;
    }
}
