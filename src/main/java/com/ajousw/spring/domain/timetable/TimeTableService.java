package com.ajousw.spring.domain.timetable;

import com.ajousw.spring.domain.ErrorMessage;
import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.member.repository.MemberRepository;
import com.ajousw.spring.domain.timetable.everytime.EveryTimeApi;
import com.ajousw.spring.domain.timetable.everytime.EveryTimeParser;
import com.ajousw.spring.domain.timetable.parser.TableInfo;
import com.ajousw.spring.domain.timetable.repository.*;
import com.ajousw.spring.web.controller.dto.timetable.SubjectDto;
import com.ajousw.spring.web.controller.dto.timetable.SubjectTimeDto;
import com.ajousw.spring.web.controller.dto.timetable.TimeTableDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TimeTableService {

    private final MemberRepository memberRepository;
    private final TimeTableRepository timeTableRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectTimeRepository subjectTimeRepository;
    private final EveryTimeApi everyTimeApi;
    private final EveryTimeParser everyTimeParser;
    private final EntityManager entityManager;
    private final AlarmSetter alarmSetter;

    public void saveTimeTable(String identifier, String userEmail) {
        Member member = findMemberByEmail(userEmail);

        deleteTimeTable(member);

        TableInfo tableInfo = everyTimeApi.getTimeTable(identifier);
        TimeTable timeTable = everyTimeParser.getTimeTable(tableInfo, member);
        List<Subject> subjects = everyTimeParser.getSubject(tableInfo, timeTable);

        saveTimeTable(timeTable, subjects);
        alarmSetter.createAlarmWithSubject(subjects, member);
    }

    private void deleteTimeTable(Member member) {
        TimeTable timeTable = member.getTimeTable();
        if (timeTable == null) {
            return;
        }

        List<SubjectTime> subjectTimes = new ArrayList<>();
        List<Subject> allSubjects = subjectRepository.findAllByTimeTableFetchSubjectTimes(timeTable);
        for (Subject subject : allSubjects) {
            subjectTimes.addAll(subject.getSubjectTimes());
        }

        subjectTimeRepository.deleteAllInBatch(subjectTimes);
        alarmSetter.deleteAlarmWithMember(member);
        subjectRepository.deleteAllInBatch(allSubjects);
        timeTableRepository.delete(timeTable);
        entityManager.flush();
    }

    private void saveTimeTable(TimeTable timeTable, List<Subject> subjects) {
        timeTableRepository.save(timeTable);
        subjectRepository.saveAll(subjects);
    }

    public TimeTableDto getTimeTable(String email) {
        Member member = findMemberByEmail(email);

        if (member.getTimeTable() == null) {
            throw new IllegalArgumentException(ErrorMessage.TIMETABLE_NOT_FOUND);
        }

        return createDto(member.getTimeTable());
    }

    public void deleteTimeTable(String email) {
        Member member = findMemberByEmail(email);
        deleteTimeTable(member);
    }

    private Member findMemberByEmail(String userEmail) {
        return memberRepository.findByEmail(userEmail).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND));
    }

    private TimeTableDto createDto(TimeTable timeTableEntity) {
        List<Subject> subjectList = subjectRepository.findAllByTimeTableFetchSubjectTimes(timeTableEntity);

        List<SubjectDto> subjectDtoList = subjectList.stream()
                .map(subject -> new SubjectDto(
                        subject.getSubjectId(),
                        subject.getEveryTimeSubjectId(),
                        subject.getCode(),
                        subject.getName(),
                        subject.getProfessor(),
                        timeTableEntity.getTableId(),
                        subject.getSubjectTimes().stream()
                                .map(subjectTime -> new SubjectTimeDto(
                                        subjectTime.getId(),
                                        subjectTime.getDay(),
                                        subjectTime.getStartTime(),
                                        subjectTime.getEndTime(),
                                        subjectTime.getPlace(),
                                        subject.getSubjectId()
                                )).collect(Collectors.toList())
                )).collect(Collectors.toList());

        return TimeTableDto.builder()
                .tableId(timeTableEntity.getTableId())
                .memberId(timeTableEntity.getMember().getId())
                .year(timeTableEntity.getYear())
                .semester(timeTableEntity.getSemester())
                .identifier(timeTableEntity.getIdentifier())
                .subjectList(subjectDtoList)
                .build();
    }

}
