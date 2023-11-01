package com.ajousw.spring.domain.timetable.repository;

import com.ajousw.spring.domain.member.repository.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subject extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    private Long subjectRealId;

    private String code;

    private String name;

    private String professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_table_id")
    private TimeTable timeTable;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<SubjectTime> subjectTimes = new ArrayList<>();

    @Builder
    public Subject(TimeTable timeTable, Long subjectRealId, String code, String name, String professor) {
        this.timeTable = timeTable;
        this.subjectRealId = subjectRealId;
        this.code = code;
        this.name = name;
        this.professor = professor;
    }

    public void addSubjectTime(SubjectTime subjectTime) {
        subjectTimes.add(subjectTime);
    }
}
