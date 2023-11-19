package com.ajousw.spring.web.controller;

import com.ajousw.spring.domain.alarm.AlarmService;
import com.ajousw.spring.domain.member.security.UserPrinciple;
import com.ajousw.spring.web.controller.dto.alarm.AlarmCreateDto;
import com.ajousw.spring.web.controller.dto.alarm.AlarmDeleteDto;
import com.ajousw.spring.web.controller.dto.alarm.AlarmDto;
import com.ajousw.spring.web.controller.dto.alarm.AlarmUpdateDto;
import com.ajousw.spring.web.controller.json.ApiResponseJson;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping("/alarm/save")
    public ApiResponseJson saveAlarm(@RequestBody @Valid AlarmCreateDto createDto,
                                     @AuthenticationPrincipal UserPrinciple user) {
        log.info("[ALARM-CREATE] : <{}>", createDto);

        alarmService.save(createDto, user.getEmail());

        log.info("[SUCCESS]");
        return new ApiResponseJson(HttpStatus.OK, "OK");
    }

    @PostMapping("/alarm/delete")
    public ApiResponseJson deleteAlarm(@RequestBody @Valid AlarmDeleteDto deleteDto,
                                       @AuthenticationPrincipal UserPrinciple user) {
        log.info("[ALARM-DELETE] : <{}>", deleteDto);

        alarmService.deleteAlarm(deleteDto, user.getEmail());

        log.info("[SUCCESS]");
        return new ApiResponseJson(HttpStatus.OK, "OK");
    }

    @PostMapping("/alarm/update")
    public ApiResponseJson updateAlarm(@RequestBody @Valid AlarmUpdateDto updateDto,
                                       @AuthenticationPrincipal UserPrinciple user) {
        log.info("[ALARM-UPDATE] : <{}>", updateDto);

        alarmService.updateAlarm(updateDto, user.getEmail());

        log.info("[SUCCESS]");
        return new ApiResponseJson(HttpStatus.OK, "OK");
    }

    @GetMapping("/alarm")
    public ApiResponseJson getAlarm(@AuthenticationPrincipal UserPrinciple user) {
        log.info("[ALARM-GET] : <EMAIL: {}>", user.getEmail());

        List<AlarmDto> alarms = alarmService.getAllAlarmByEmail(user.getEmail());

        log.info("[SUCCESS]");
        return new ApiResponseJson(HttpStatus.OK, Map.of("alarms", alarms));
    }

}
