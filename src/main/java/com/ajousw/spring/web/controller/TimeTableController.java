package com.ajousw.spring.web.controller;

import com.ajousw.spring.domain.member.security.UserPrinciple;
import com.ajousw.spring.domain.timetable.TimeTableService;
import com.ajousw.spring.web.controller.dto.timetable.TimeTableDto;
import com.ajousw.spring.web.controller.json.ApiResponseJson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TimeTableController {

    private final TimeTableService timeTableService;

    @GetMapping("/timetable/save")
    public ApiResponseJson save(@Param("identifier") String identifier, @AuthenticationPrincipal UserPrinciple user) {
        timeTableService.saveTimeTableAndSubject(identifier, user.getEmail());
        return new ApiResponseJson(HttpStatus.OK, "done");
    }

    @PostMapping("/timetable/delete")
    public ApiResponseJson delete(@AuthenticationPrincipal UserPrinciple user) {
        timeTableService.deleteTimeTableAndSubject(user.getEmail());
        return new ApiResponseJson(HttpStatus.OK, "done");
    }

    @GetMapping("/timetable")
    public ApiResponseJson get(@AuthenticationPrincipal UserPrinciple user) {
        TimeTableDto timeTable = timeTableService.getTimeTable(user.getEmail());
        return new ApiResponseJson(HttpStatus.OK, timeTable);
    }
}
