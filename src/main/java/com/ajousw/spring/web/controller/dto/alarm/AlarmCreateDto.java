package com.ajousw.spring.web.controller.dto.alarm;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmCreateDto {

    @NotNull
    private Long subjectId;

    @NotNull
    private Long day;

    @NotNull
    private Long hour;

    @NotNull
    private Long minute;

    private Long alarmGap;

    private Boolean isAlarmOn;
}
