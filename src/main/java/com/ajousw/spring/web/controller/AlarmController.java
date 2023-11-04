package com.ajousw.spring.web.controller;

import com.ajousw.spring.domain.alarm.AlarmService;
import com.ajousw.spring.domain.member.security.UserPrinciple;
import com.ajousw.spring.web.controller.dto.alarm.AlarmCreateDto;
import com.ajousw.spring.web.controller.dto.alarm.AlarmDeleteDto;
import com.ajousw.spring.web.controller.dto.alarm.AlarmUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping("/alarm/save")
    public void saveAlarm(@RequestBody @Valid AlarmCreateDto createDto,
                          @AuthenticationPrincipal UserPrinciple user) {
        log.info("[ALARM-CREATE] : <{}>", createDto);

        alarmService.save(createDto, user.getEmail());

        log.info("[SUCCESS]");
    }

    @PostMapping("/alarm/delete")
    public void deleteAlarm(@RequestBody @Valid AlarmDeleteDto deleteDto,
                            @AuthenticationPrincipal UserPrinciple user) {
        log.info("[ALARM-DELETE] : <{}>", deleteDto);

        alarmService.deleteAlarm(deleteDto, user.getEmail());

        log.info("[SUCCESS]");
    }

    @PostMapping("/alarm/update")
    public void updateAlarm(@RequestBody @Valid AlarmUpdateDto updateDto,
                            @AuthenticationPrincipal UserPrinciple user) {
        log.info("[ALARM-UPDATE] : <{}>", updateDto);

        alarmService.updateAlarm(updateDto, user.getEmail());

        log.info("[SUCCESS]");
    }

}
