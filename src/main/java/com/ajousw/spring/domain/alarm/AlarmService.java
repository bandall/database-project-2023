package com.ajousw.spring.domain.alarm;

import com.ajousw.spring.domain.ErrorMessage;
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
import java.util.Objects;
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
        if (alarmCreateDto.getSubjectId() == -1) {
            saveWithNewSubject(alarmCreateDto, email);
            return;
        }

        saveWithSubject(alarmCreateDto, email);
    }

    private void saveWithSubject(AlarmCreateDto alarmCreateDto, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND));

        Subject subject = subjectRepository.findById(alarmCreateDto.getSubjectId()).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.SUBJECT_NOT_FOUND));

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

    private void saveWithNewSubject(AlarmCreateDto alarmCreateDto, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND));

        Subject customSubject = Subject.builder()
                .subjectRealId(0L)
                .code("CUSTOM_SUBJECT")
                .name(alarmCreateDto.getName())
                .professor(email)
                .timeTable(member.getTimeTable())
                .build();

        subjectRepository.save(customSubject);

        Alarm newAlarm = Alarm.builder()
                .member(member)
                .subject(customSubject)
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

        return alarm.stream().map(this::getAlarmSubjectDto)
                .collect(Collectors.toList());
    }

    public AlarmDto getAlarmById(Long id) {
        Alarm alarm = alarmRepository.findByIdFetch(id).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.ALARM_NOT_FOUND));

        return getAlarmSubjectDto(alarm);
    }

    public void updateAlarm(AlarmUpdateDto alarmModifyDto, String email) {
        Alarm alarm = alarmRepository.findByIdFetch(alarmModifyDto.getAlarmId()).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.ALARM_NOT_FOUND));

        checkOwner(email, alarm);

        alarm.setAlarmOn(alarmModifyDto.getIsAlarmOn());
        alarm.setAlarmGap(alarmModifyDto.getAlarmGap());
    }

    public void deleteAlarm(AlarmDeleteDto alarmDeleteDto, String email) {
        Alarm alarm = alarmRepository.findByIdFetch(alarmDeleteDto.getAlarmId()).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.ALARM_NOT_FOUND));

        checkOwner(email, alarm);

        alarmRepository.deleteById(alarmDeleteDto.getAlarmId());
    }

    private void checkOwner(String email, Alarm alarm) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND));

        if (!Objects.equals(alarm.getMember().getId(), member.getId())) {
            throw new IllegalArgumentException(ErrorMessage.NOT_OWNER);
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
