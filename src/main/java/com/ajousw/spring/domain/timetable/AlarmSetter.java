package com.ajousw.spring.domain.timetable;

import com.ajousw.spring.domain.alarm.repository.Alarm;
import com.ajousw.spring.domain.alarm.repository.AlarmRepository;
import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.timetable.repository.Subject;
import com.ajousw.spring.domain.timetable.repository.SubjectTime;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class AlarmSetter {

    private final AlarmRepository alarmRepository;

    public void createAlarmWithSubject(List<Subject> subjects, Member member) {
        alarmRepository.deleteByMember(member);
        alarmRepository.flush();

        List<SubjectTime> allSubjectTimes = subjects.stream()
                .flatMap(subject -> subject.getSubjectTimes().stream())
                .toList();

        for (Subject subject : subjects) {
            for (SubjectTime subjectTime : subject.getSubjectTimes()) {
                Alarm newAlarm = Alarm.builder()
                        .day((long) subjectTime.getDay())
                        .hour(getHour(subjectTime.getStartTime()))
                        .minute(getMinute(subjectTime.getStartTime()))
                        .alarmGap(calculateGap(allSubjectTimes, subjectTime))
                        .isAlarmOn(true)
                        .member(member)
                        .subject(subject)
                        .build();
                alarmRepository.save(newAlarm);
            }
        }
    }

    private Long calculateGap(List<SubjectTime> allSubjectTimes, SubjectTime subjectTime) {
        int startTime = subjectTime.getStartTime();
        if (getHour(startTime) == 9 && getMinute(startTime) == 0) {
            return 10L;
        }

        List<SubjectTime> todaySubject = allSubjectTimes.stream()
                .filter(time -> time.getDay() == subjectTime.getDay()
                        && time.getEndTime() <= subjectTime.getStartTime())
                .toList();

        for (SubjectTime time : todaySubject) {
            int endTime = time.getEndTime();

            if (startTime - endTime <= 3) {
                return 0L;
            }
        }

        return 15L;
    }

    public void deleteAlarmWithMember(Member member) {
        alarmRepository.deleteByMember(member);
    }

    private Long getHour(int timeUnit) {
        return (long) (timeUnit / 12);
    }

    private Long getMinute(int timeUnit) {
        return (long) (timeUnit % 12 * 5);
    }
}
