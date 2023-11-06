package com.ajousw.spring.domain.alarm;

import com.ajousw.spring.domain.alarm.repository.Alarm;
import com.ajousw.spring.domain.alarm.repository.AlarmRepository;
import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.member.repository.MemberRepository;
import com.ajousw.spring.domain.timetable.repository.Subject;
import com.ajousw.spring.domain.timetable.repository.SubjectRepository;
import com.ajousw.spring.web.controller.dto.alarm.AlarmCreateDto;
import com.ajousw.spring.web.controller.dto.alarm.AlarmDeleteDto;
import com.ajousw.spring.web.controller.dto.alarm.AlarmDto;
import com.ajousw.spring.web.controller.dto.alarm.AlarmUpdateDto;
import com.ajousw.spring.web.controller.dto.timetable.SubjectDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final SubjectRepository subjectRepository;
    private final MemberRepository memberRepository;

    public void save(AlarmCreateDto alarmCreateDto, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 유저입니다."));

        Subject subject = subjectRepository.findById(alarmCreateDto.getSubjectId()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 과목입니다."));

        Alarm newAlarm = Alarm.builder()
                .member(member)
                .subject(subject)
                .day(alarmCreateDto.getDay())
                .hour(alarmCreateDto.getHour())
                .minute(alarmCreateDto.getMinute())
                .alarmGap(alarmCreateDto.getAlarmGap())
                .isAlarmOn(alarmCreateDto.getIsAlarmOn())
                .build();

        alarmRepository.save(newAlarm);
    }

    public List<AlarmDto> getAllAlarmByEmail(String email) {
        List<Alarm> alarm = alarmRepository.findByMemberEmailFetch(email);

        return alarm.stream().map(a -> getAlarmSubjectDto(a))
                .collect(Collectors.toList());
    }

    public AlarmDto getAlarmById(Long id) {
        Alarm alarm = alarmRepository.findByIdFetch(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 알람입니다."));

        return getAlarmSubjectDto(alarm);
    }

    public void updateAlarm(AlarmUpdateDto alarmModifyDto, String email) {
        Alarm alarm = alarmRepository.findByIdFetch(alarmModifyDto.getAlarmId()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 알람입니다."));

        checkOwner(email, alarm);

        alarm.setAlarmOn(alarmModifyDto.getIsAlarmOn());
        alarm.setAlarmGap(alarm.getAlarmGap());
    }

    public void deleteAlarm(AlarmDeleteDto alarmDeleteDto, String email) {
        Alarm alarm = alarmRepository.findByIdFetch(alarmDeleteDto.getAlarmId()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 알람입니다."));

        checkOwner(email, alarm);

        alarmRepository.deleteById(alarmDeleteDto.getAlarmId());
    }

    private void checkOwner(String email, Alarm alarm) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 유저입니다."));

        if (alarm.getMember().getId() != member.getId()) {
            throw new IllegalArgumentException("알람은 본인만 삭제할 수 있습니다.");
        }
    }


    private AlarmDto getAlarmSubjectDto(Alarm alarm) {
        Subject subject = alarm.getSubject();

        return new AlarmDto(
                alarm.getId(),
                alarm.getMember().getId(),
                alarm.getDay(),
                alarm.getHour(),
                alarm.getMinute(),
                alarm.getAlarmGap(),
                alarm.getIsAlarmOn(),
                new SubjectDto(subject.getSubjectId(),
                        subject.getSubjectRealId(),
                        subject.getCode(),
                        subject.getName(),
                        subject.getProfessor(),
                        subject.getTimeTable().getTableId(),
                        null)
        );
    }
}
