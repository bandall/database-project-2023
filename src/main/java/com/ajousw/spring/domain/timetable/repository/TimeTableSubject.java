package com.ajousw.spring.domain.timetable.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeTableSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "join_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    private TimeTable timeTable;

    @Builder
    public TimeTableSubject(Subject subject, TimeTable timeTable) {
        this.subject = subject;
        this.timeTable = timeTable;
    }
}
