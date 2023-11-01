package com.ajousw.spring.web.controller.dto.TimeTable;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class TimeTableDto {
    private Long tableId;
    private Long memberId;
    private String year;
    private String semester;
    private String identifier;
    private List<SubjectDto> subjectList;

    @Builder
    public TimeTableDto(Long tableId, Long memberId, String year, String semester, String identifier, List<SubjectDto> subjectList) {
        this.tableId = tableId;
        this.memberId = memberId;
        this.year = year;
        this.semester = semester;
        this.identifier = identifier;
        this.subjectList = subjectList;
    }
}