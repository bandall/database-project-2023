package com.ajousw.spring.domain.timetable.repository;

import com.ajousw.spring.domain.member.repository.BaseTimeEntity;
import com.ajousw.spring.domain.member.repository.Member;
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
public class TimeTable extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String year;

    private String semester;

    private String identifier;

    @OneToMany(mappedBy = "timeTable", cascade = CascadeType.ALL)
    private List<Subject> subjectList = new ArrayList<>();

    @Builder
    public TimeTable(Member member, String year, String semester, String identifier) {
        this.member = member;
        this.year = year;
        this.semester = semester;
        this.identifier = identifier;
    }

    public void addSubject(Subject subject) {
        subjectList.add(subject);
    }
}
