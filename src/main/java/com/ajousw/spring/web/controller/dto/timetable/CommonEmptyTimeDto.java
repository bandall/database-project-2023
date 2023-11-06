package com.ajousw.spring.web.controller.dto.timetable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonEmptyTimeDto {
    @NotNull
    List<String> emails;

    @NotNull
    Integer minTimeDiff;

    @Min(value = 0)
    @Max(value = 288)
    Integer startTime;

    @Min(value = 0)
    @Max(value = 289)
    Integer endTime;
}
