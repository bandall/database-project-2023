package com.ajousw.spring.domain.timetable;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@Slf4j
@WebMvcTest
class TimeTableServiceTest {

    @Autowired
    TimeTableService timeTableService;

    @Test
    public void test() {
        String identifier = "50HePCkWpfmOvrVaA4A3";

        TimeTable timeTable = timeTableService.getTimeTable(identifier);
        log.info("year={}, semester={}, identifier={}", timeTable.getYear(), timeTable.getSemester(),
                timeTable.getIdentifier());

        for (Subject subject : timeTable.getSubjectList()) {
            log.info("{}", subject);
        }

    }
}