package com.ajousw.spring.web.controller.dto.alarm;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmSubjectDto {

    private Long alarmId;

    private Long memberId;

    private Long subjectId;

    private Long day;

    private Long hour;

    private Long minute;

    private Long alarmGap;

    private Boolean isAlarmOn;

    private Long subjectRealId;

    private String code;

    private String name;

    private String professor;
}
