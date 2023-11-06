package com.ajousw.spring.web.controller;

import com.ajousw.spring.domain.tablejoin.Day;
import com.ajousw.spring.domain.tablejoin.TableJoinService;
import com.ajousw.spring.web.controller.dto.timetable.CommonEmptyTimeDto;
import com.ajousw.spring.web.controller.json.ApiResponseJson;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TableJoinController {

    private final TableJoinService tableJoinService;

    @PostMapping("/timetable/empty-time")
    public ApiResponseJson findCommonEmptyTime(@Valid @RequestBody CommonEmptyTimeDto commonEmptyTimeDto) {
        Map<Day, List<String>> commonEmptyTimes = tableJoinService.findCommonEmptyTime(commonEmptyTimeDto);
        return new ApiResponseJson(HttpStatus.OK, commonEmptyTimes);
    }

}
