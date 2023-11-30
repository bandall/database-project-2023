package com.ajousw.spring.domain.timetable;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
class TimeTableServiceTest {

    @Autowired
    private TimeTableService timeTableService;

    @Test
    @Transactional
    public void getTimeTableWithNoFetch() {
        String email = "jsm5315@gmail.com";

        log.info("[QUERY START]");
        timeTableService.getTimeTableWithNoFetch(email);
        log.info("[QUERY END]");
    }

    @Test
    public void getTimeTableWithFetch() {
        String email = "jsm5315@gmail.com";

        log.info("[QUERY START]");
        timeTableService.getTimeTable(email);
        log.info("[QUERY END]");
    }

}