package com.ajousw.spring.web.controller.dto.timetable;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class SubjectDto {
    private Long subjectId;
    private Long subjectRealId;
    private String code;
    private String name;
    private String professor;
    private Long timeTableId;
    private List<SubjectTimeDto> subjectTimes;

    @Builder
    public SubjectDto(Long subjectId, Long subjectRealId, String code, String name, String professor, Long timeTableId, List<SubjectTimeDto> subjectTimes) {
        this.subjectId = subjectId;
        this.subjectRealId = subjectRealId;
        this.code = code;
        this.name = name;
        this.professor = professor;
        this.timeTableId = timeTableId;
        this.subjectTimes = subjectTimes;
    }
}
