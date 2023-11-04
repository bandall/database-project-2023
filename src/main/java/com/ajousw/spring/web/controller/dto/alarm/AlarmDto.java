package com.ajousw.spring.web.controller.dto.alarm;

import com.ajousw.spring.web.controller.dto.timetable.SubjectDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmDto {

    private Long alarmId;

    private Long memberId;

    private Long day;

    private Long hour;

    private Long minute;

    private Long alarmGap;

    private Boolean isAlarmOn;

    private SubjectDto subjectDto;
}
