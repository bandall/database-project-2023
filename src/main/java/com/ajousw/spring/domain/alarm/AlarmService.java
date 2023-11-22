package com.ajousw.spring.domain.alarm;

import com.ajousw.spring.domain.ErrorMessage;
import com.ajousw.spring.domain.alarm.repository.Alarm;
import com.ajousw.spring.domain.alarm.repository.AlarmRepository;
import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.member.repository.MemberRepository;
import com.ajousw.spring.domain.timetable.repository.Subject;
import com.ajousw.spring.domain.timetable.repository.SubjectRepository;
import com.ajousw.spring.domain.timetable.repository.TimeTableSubject;
import com.ajousw.spring.domain.timetable.repository.TimeTableSubjectRepository;
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
    private final TimeTableSubjectRepository timeTableSubjectRepository;

    public void createNewSubject(AlarmCreateDto alarmCreateDto, String email) {
        Member member = findByMemberFetchTimeTable(email);

        if (member.getTimeTable() == null) {
            throw new IllegalStateException(ErrorMessage.TIMETABLE_NOT_FOUND);
        }

        if (alarmCreateDto.getSubjectId() == -1) {
            saveWithNewSubject(alarmCreateDto, member);
            return;
        }

        saveWithSubject(alarmCreateDto, member);
    }

    private void saveWithSubject(AlarmCreateDto alarmCreateDto, Member member) {

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

    private void saveWithNewSubject(AlarmCreateDto alarmCreateDto, Member member) {
        Subject customSubject = Subject.builder()
                .everyTimeSubjectId(-1L)
                .code("CUSTOM_SUBJECT")
                .name(alarmCreateDto.getName())
                .professor(member.getEmail())
                .build();
        subjectRepository.save(customSubject);

        TimeTableSubject timeTableSubject = new TimeTableSubject(member.getTimeTable(), customSubject);
        timeTableSubjectRepository.save(timeTableSubject);

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

    // TODO: 커스텀 subject 로직 결정
    public void deleteAlarm(AlarmDeleteDto alarmDeleteDto, String email) {
        Alarm alarm = alarmRepository.findByIdFetch(alarmDeleteDto.getAlarmId()).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.ALARM_NOT_FOUND));

        checkOwner(email, alarm);

        alarmRepository.deleteById(alarmDeleteDto.getAlarmId());
    }

    private void checkOwner(String email, Alarm alarm) {
        Member member = findByMemberFetchTimeTable(email);

        if (!Objects.equals(alarm.getMember().getId(), member.getId())) {
            throw new IllegalArgumentException(ErrorMessage.NOT_OWNER);
        }
    }

    private Member findByMemberFetchTimeTable(String email) {
        return memberRepository.findByEmailFetchTimeTable(email).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND));
    }

    // TODO: 양방향 매핑 필요한지 체크 후 수정
    private Member findByMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND));
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
                        subject.getEveryTimeSubjectId(),
                        subject.getCode(),
                        subject.getName(),
                        subject.getProfessor(),
                        null)
        );
    }
}
