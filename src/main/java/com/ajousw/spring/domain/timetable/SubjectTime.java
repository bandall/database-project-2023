package com.ajousw.spring.domain.timetable;

import lombok.Data;

@Data
public class SubjectTime {

    private int day;
    private int startTime;
    private int endTime;
    private String place;

}
