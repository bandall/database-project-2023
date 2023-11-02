package com.ajousw.spring.domain.timetable;

import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.member.repository.MemberRepository;
import com.ajousw.spring.domain.timetable.exception.EverytimeParsingException;
import com.ajousw.spring.domain.timetable.parser.ClassTime;
import com.ajousw.spring.domain.timetable.parser.SubjectParse;
import com.ajousw.spring.domain.timetable.parser.TableInfo;
import com.ajousw.spring.domain.timetable.parser.TableParse;
import com.ajousw.spring.domain.timetable.repository.Subject;
import com.ajousw.spring.domain.timetable.repository.SubjectTime;
import com.ajousw.spring.domain.timetable.repository.TimeTable;
import com.ajousw.spring.domain.timetable.repository.TimeTableRepository;
import com.ajousw.spring.web.controller.dto.timetable.SubjectDto;
import com.ajousw.spring.web.controller.dto.timetable.SubjectTimeDto;
import com.ajousw.spring.web.controller.dto.timetable.TimeTableDto;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TimeTableService {

    private final MemberRepository memberJpaRepository;
    private final TimeTableRepository timeTableRepository;
    private WebClient webClient;
    private XmlMapper xmlMapper;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder().build();
        this.xmlMapper = new XmlMapper();
    }

    public void saveTimeTable(String identifier, String userEmail) {
        String param = "?identifier=" + identifier + "&friendInfo=true";
        ResponseEntity<String> response = null;
        try {
            response = webClient.post()
                    .uri("https://api.everytime.kr/find/timetable/table/friend" + param)
                    .header("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36")
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new EverytimeParsingException("에브리타임 API 사용 중 오류가 발생했습니다", e);
        }

        Member member = memberJpaRepository.findByEmail(userEmail).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 유저입니다"));

        TimeTable timeTable = parseTimeTable(response.getBody(), member);
        timeTableRepository.save(timeTable);
    }

    public TimeTableDto getTimeTable(String email) {
        Member member = memberJpaRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 유저입니다."));

        TimeTable timeTable = timeTableRepository.findByMember(member).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 테이블입니다."));

        return convertToDto(timeTable);
    }

    public void deleteTimeTable(String email) {
        Member member = memberJpaRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 유저입니다."));

        timeTableRepository.deleteByMember(member);
    }

    private TimeTable parseTimeTable(String xml, Member member) {
        try {
            TableInfo tableInfo = xmlMapper.readValue(xml, TableInfo.class);
            return convert(tableInfo, member);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    private TimeTable convert(TableInfo tableInfo, Member member) {
        TableParse parsedTable = tableInfo.getTables().get(0);

        TimeTable timeTable = TimeTable.builder()
                .member(member)
                .identifier(parsedTable.getIdentifier())
                .year(parsedTable.getYear())
                .semester(parsedTable.getSemester())
                .build();

        for (SubjectParse subjectParse : parsedTable.getSubjects()) {
            Subject subject = setSubject(subjectParse, timeTable);

            timeTable.addSubject(subject);
        }

        return timeTable;
    }

    private Subject setSubject(SubjectParse subjectParse, TimeTable timeTable) {
        Subject subject = Subject.builder()
                .timeTable(timeTable)
                .subjectRealId(Long.valueOf(subjectParse.getId()))
                .name(subjectParse.getName())
                .code(subjectParse.getInternal())
                .professor(subjectParse.getProfessor())
                .build();

        for (ClassTime classTime : subjectParse.getClassTimes()) {
            SubjectTime subjectTime = setSubjectTime(classTime, subject);
            subject.addSubjectTime(subjectTime);
        }
        return subject;
    }

    private SubjectTime setSubjectTime(ClassTime classTime, Subject subject) {
        SubjectTime subjectTime = SubjectTime.builder()
                .subject(subject)
                .day(classTime.getDay())
                .place(classTime.getPlace())
                .startTime(classTime.getStartTime())
                .endTime(classTime.getEndTime())
                .build();

        return subjectTime;
    }

    private TimeTableDto convertToDto(TimeTable timeTableEntity) {
        List<SubjectDto> subjectDtoList = timeTableEntity.getSubjectList().stream()
                .map(subject -> new SubjectDto(
                        subject.getSubjectId(),
                        subject.getSubjectRealId(),
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
