package com.ajousw.spring.domain.timetable;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Subject {
    private int id;
    private String code;
    private String name;
    private String professor;
    private List<SubjectTime> subjectTimes = new ArrayList<>();

    public void addSubjectTime(SubjectTime subjectTime) {
        subjectTimes.add(subjectTime);
    }
}
