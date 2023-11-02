package com.ajousw.spring.domain.alarm;

import com.ajousw.spring.domain.alarm.repository.Alarm;
import com.ajousw.spring.domain.alarm.repository.AlarmRepository;
import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.member.repository.MemberRepository;
import com.ajousw.spring.domain.timetable.repository.Subject;
import com.ajousw.spring.domain.timetable.repository.SubjectRepository;
import com.ajousw.spring.web.controller.dto.alarm.AlarmCreateDto;
import com.ajousw.spring.web.controller.dto.alarm.AlarmDeleteDto;
import com.ajousw.spring.web.controller.dto.alarm.AlarmModifyDto;
import com.ajousw.spring.web.controller.dto.alarm.AlarmSubjectDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final SubjectRepository subjectRepository;
    private final MemberRepository memberRepository;

    private static AlarmSubjectDto getAlarmSubjectDto(Alarm alarm) {
        Subject subject = alarm.getSubject();

        return new AlarmSubjectDto(
                alarm.getId(),
                alarm.getMember().getId(),
                subject.getSubjectId(),
                alarm.getDay(),
                alarm.getHour(),
                alarm.getMinute(),
                alarm.getAlarmGap(),
                alarm.getIsAlarmOn(),
                subject.getSubjectRealId(),
                subject.getCode(),
                subject.getName(),
                subject.getProfessor()
        );
    }

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

    public List<AlarmSubjectDto> getAllAlarmByEmail(String email) {
        List<Alarm> alarm = alarmRepository.findByMemberEmailFetch(email);

        List<AlarmSubjectDto> dtoList = alarm.stream().map(a -> getAlarmSubjectDto(a))
                .collect(Collectors.toList());

        return dtoList;
    }

    public AlarmSubjectDto getAlarmById(Long id) {
        Alarm alarm = alarmRepository.findByIdFetch(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 알람입니다."));

        return getAlarmSubjectDto(alarm);
    }

    public void modifyAlarm(AlarmModifyDto alarmModifyDto) {
        Alarm alarm = alarmRepository.findByIdFetch(alarmModifyDto.getAlarmId()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 알람입니다."));

        alarm.setAlarmOn(alarmModifyDto.getIsAlarmOn());
        alarm.setAlarmGap(alarm.getAlarmGap());
    }

    public void deleteAlarm(AlarmDeleteDto alarmDeleteDto) {
        alarmRepository.deleteById(alarmDeleteDto.getAlarmId());
    }

}
