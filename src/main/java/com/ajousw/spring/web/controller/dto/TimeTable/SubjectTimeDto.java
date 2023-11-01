package com.ajousw.spring.web.controller.dto.TimeTable;

import lombok.Builder;
import lombok.Data;

@Data
public class SubjectTimeDto {
    private Long id;
    private int day;
    private int startTime;
    private int endTime;
    private String place;
    private Long subjectId;

    @Builder
    public SubjectTimeDto(Long id, int day, int startTime, int endTime, String place, Long subjectId) {
        this.id = id;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.subjectId = subjectId;
    }
}