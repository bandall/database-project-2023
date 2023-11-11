package com.ajousw.spring.domain.timetable;

import com.ajousw.spring.domain.alarm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoAlarmSettingService {

    private final AlarmRepository alarmRepository;

//    public void createAlarmWithTimeTable(TimeTable timeTable) {
//        List<Subject> subjectList = timeTable.getSubjectList();
//
//        alarmRepository.deleteByMember(timeTable.getMember());
//
//    }

//    private List<Alarm> createAlarmWithSubject(List<Subject> subjectList) {
//
//        for (Subject subject : subjectList) {
//
//        }
//    }

}
