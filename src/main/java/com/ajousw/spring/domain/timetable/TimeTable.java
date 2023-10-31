package com.ajousw.spring.domain.timetable;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TimeTable {
    private String year;
    private String semester;
    private String identifier;

    private List<Subject> subjectList = new ArrayList<>();

    public void addSubject(Subject subject) {
        subjectList.add(subject);
    }
}
