package com.ajousw.spring.domain.timetable;

import com.ajousw.spring.domain.ErrorMessage;
import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.member.repository.MemberRepository;
import com.ajousw.spring.domain.timetable.everytime.EveryTimeApi;
import com.ajousw.spring.domain.timetable.everytime.EveryTimeParser;
import com.ajousw.spring.domain.timetable.parser.TableInfo;
import com.ajousw.spring.domain.timetable.repository.Subject;
import com.ajousw.spring.domain.timetable.repository.SubjectRepository;
import com.ajousw.spring.domain.timetable.repository.TimeTable;
import com.ajousw.spring.domain.timetable.repository.TimeTableRepository;
import com.ajousw.spring.domain.timetable.repository.TimeTableSubject;
import com.ajousw.spring.domain.timetable.repository.TimeTableSubjectRepository;
import com.ajousw.spring.web.controller.dto.timetable.SubjectDto;
import com.ajousw.spring.web.controller.dto.timetable.SubjectTimeDto;
import com.ajousw.spring.web.controller.dto.timetable.TimeTableDto;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TimeTableService {

    private final MemberRepository memberRepository;
    private final TimeTableRepository timeTableRepository;
    private final SubjectRepository subjectRepository;
    private final TimeTableSubjectRepository timeTableSubjectRepository;

    private final EveryTimeApi everyTimeApi;
    private final EveryTimeParser everyTimeParser;
    private final EntityManager entityManager;
    private final AlarmSetter alarmSetter;

    public void saveTimeTableAndSubject(String identifier, String userEmail) {
        Member member = findMemberByEmailFetchTimeTable(userEmail);

        deleteTimeTableAndSubject(member);

        TableInfo tableInfo = everyTimeApi.getTimeTable(identifier);
        TimeTable timeTable = everyTimeParser.getTimeTable(tableInfo, member);
        List<Subject> subjects = everyTimeParser.getSubject(tableInfo);

        saveTimeTableAndSubject(timeTable, subjects);
        alarmSetter.createAlarmWithSubject(subjects, member);
    }

    public void deleteTimeTableAndSubject(String email) {
        Member member = findMemberByEmailFetchTimeTable(email);
        deleteTimeTableAndSubject(member);
    }

    private void saveTimeTableAndSubject(TimeTable newTimeTable, List<Subject> newSubjects) {
        timeTableRepository.save(newTimeTable);

        saveSubject(newSubjects);

        newSubjects.forEach(subject -> {
            timeTableSubjectRepository.save(new TimeTableSubject(newTimeTable, subject));
        });
    }

    private void deleteTimeTableAndSubject(Member member) {
        TimeTable timeTable = member.getTimeTable();
        if (timeTable == null) {
            return;
        }

        alarmSetter.deleteAlarmWithMember(member);
        timeTableSubjectRepository.deleteAllByTimeTable(timeTable);
        timeTableRepository.delete(timeTable);
        entityManager.flush();
    }

    private void saveSubject(List<Subject> newSubjects) {
        List<Long> newEveryTimeSubjectIds = newSubjects.stream()
                .map(Subject::getEveryTimeSubjectId).toList();

        Map<Long, Long> subjectIdInDb = subjectRepository.findAllByEveryTimeSubjectIdIs(newEveryTimeSubjectIds)
                .stream().collect(Collectors.toMap(Subject::getEveryTimeSubjectId, Subject::getSubjectId));

        List<Subject> subjectsToSave = new ArrayList<>();

        for (Subject newSubject : newSubjects) {
            Long subjectInDb = subjectIdInDb.get(newSubject.getEveryTimeSubjectId());
            if (subjectInDb != null) {
                newSubject.setSubjectId(subjectInDb);
                continue;
            }

            subjectsToSave.add(newSubject);
        }

        subjectRepository.saveAll(subjectsToSave);
    }

    @Transactional(readOnly = true)
    public TimeTableDto getTimeTable(String email) {
        TimeTable timeTable = findMemberByEmailFetchTimeTable(email).getTimeTable();

        if (timeTable == null) {
            throw new IllegalArgumentException(ErrorMessage.TIMETABLE_NOT_FOUND);
        }

        List<Long> subjectIds = timeTable.getSubjects()
                .stream().map(TimeTableSubject::getSubject)
                .map(Subject::getSubjectId)
                .toList();

        List<Subject> subjects = subjectRepository.findAllBySubjectIdIsFetchSubjectTime(subjectIds);

        return createDto(timeTable, subjects);
    }

    @Transactional(readOnly = true)
    public TimeTableDto getTimeTableWithNoFetch(String email) {
        Member member = findMemberByEmail(email);
        TimeTable timeTable = member.getTimeTable();

        if (timeTable == null) {
            throw new IllegalArgumentException(ErrorMessage.TIMETABLE_NOT_FOUND);
        }

        List<Subject> subjects = timeTable.getSubjects().stream()
                .map(TimeTableSubject::getSubject)
                .toList();

        return createDto(timeTable, subjects);
    }

    private Member findMemberByEmail(String userEmail) {
        return memberRepository.findByEmail(userEmail).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND));
    }

    private Member findMemberByEmailFetchTimeTable(String userEmail) {
        return memberRepository.findByEmailFetchTimeTable(userEmail).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND));
    }

    private TimeTableDto createDto(TimeTable timeTable, List<Subject> subjects) {
        List<SubjectDto> subjectDtoList = subjects.stream()
                .map(subject -> new SubjectDto(
                        subject.getSubjectId(),
                        subject.getEveryTimeSubjectId(),
                        subject.getCode(),
                        subject.getName(),
                        subject.getProfessor(),
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
                .tableId(timeTable.getTableId())
                .memberId(timeTable.getMember().getId())
                .year(timeTable.getYear())
                .semester(timeTable.getSemester())
                .identifier(timeTable.getIdentifier())
                .subjectList(subjectDtoList)
                .build();
    }

}
