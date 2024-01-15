package com.ajousw.spring.domain.timetable;

import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
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

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    private int maxTest = 30;

    @Test
    @Transactional
    public void getTimeTableWithNoFetch() {
        List<Member> allMember = memberRepository.findAll();
        int curCount = 0;

        long startTime = System.currentTimeMillis();
        for (Member m : allMember) {
//            log.info("[QUERY START]");
            if (m.getTimeTable() == null) {
                continue;
            }
            timeTableService.getTimeTableWithNoFetch(m.getEmail());
//            log.info("[QUERY END]");
            entityManager.clear();

            curCount++;
            if (curCount >= maxTest) {
                break;
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("TIME => {}ms", endTime - startTime);

        entityManager.clear();
        entityManager.flush();
    }

    @Test
    @Transactional
    public void getTimeTableWithFetch() {
        List<Member> allMember = memberRepository.findAll();
        int curCount = 0;

        long startTime = System.currentTimeMillis();
        for (Member m : allMember) {
//            log.info("[QUERY START]");
            if (m.getTimeTable() == null) {
                continue;
            }
            timeTableService.getTimeTable(m.getEmail());
//            log.info("[QUERY END]");
            entityManager.clear();

            curCount++;
            if (curCount >= maxTest) {
                break;
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("TIME => {}ms", endTime - startTime);

        entityManager.clear();
        entityManager.flush();
    }

}