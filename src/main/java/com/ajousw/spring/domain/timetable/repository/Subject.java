package com.ajousw.spring.domain.timetable.repository;

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
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    private Long everyTimeSubjectId;

    private String code;

    private String name;

    private String professor;

    @OneToMany(mappedBy = "subject")
    private List<TimeTableSubject> timeTables = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<SubjectTime> subjectTimes = new ArrayList<>();

    @Builder
    public Subject(Long everyTimeSubjectId, String code, String name, String professor) {
        this.everyTimeSubjectId = everyTimeSubjectId;
        this.code = code;
        this.name = name;
        this.professor = professor;
    }

    public void addSubjectTime(SubjectTime subjectTime) {
        subjectTimes.add(subjectTime);
    }
}
