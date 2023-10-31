package com.ajousw.spring.web.controller;

import com.ajousw.spring.domain.member.MemberService;
import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.member.security.UserPrinciple;
import com.ajousw.spring.domain.timetable.TimeTable;
import com.ajousw.spring.domain.timetable.TimeTableService;
import com.ajousw.spring.web.controller.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final MemberService memberService;
    private final TimeTableService timeTableService;

    @GetMapping("/userinfo")
    public MemberDto home(@AuthenticationPrincipal UserPrinciple user) {
        Member member = memberService.getMember(user.getEmail());
        return new MemberDto(member.getEmail(), member.getUsername(), member.getLoginType(), member.getCreatedDate());
    }

    @GetMapping("/test")
    public TimeTable test(@Param("identifier") String identifier) {
        return timeTableService.getTimeTable(identifier);
    }

}
