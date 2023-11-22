package com.ajousw.spring.domain.timetable.cron;

import com.ajousw.spring.domain.timetable.repository.SubjectRepository;
import com.ajousw.spring.domain.timetable.repository.SubjectTimeRepository;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteNonReferencedSubjectScheduler {
    private final SubjectRepository subjectRepository;
    private final SubjectTimeRepository subjectTimeRepository;
    
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteNonReferencedSubject() {
        log.info("[Scheduler] [{}] Deleting Non Referenced Subject", new Timestamp(System.currentTimeMillis()));
        List<Long> nonReferencedSubjects = subjectRepository.findAllSubjectIdWhereNonReferenced();

        if (nonReferencedSubjects.isEmpty()) {
            return;
        }

        subjectTimeRepository.deleteAllBySubjectIds(nonReferencedSubjects);
        subjectRepository.deleteAllByIds(nonReferencedSubjects);
    }

}
