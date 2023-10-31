package com.ajousw.spring.domain.timetable;

import com.ajousw.spring.domain.timetable.parser.ClassTime;
import com.ajousw.spring.domain.timetable.parser.SubjectParse;
import com.ajousw.spring.domain.timetable.parser.TableInfo;
import com.ajousw.spring.domain.timetable.parser.TableParse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeTableService {
    private final WebClient webClient;
    private final XmlMapper xmlMapper;

    public TimeTableService() {
        this.webClient = WebClient.builder().build();
        xmlMapper = new XmlMapper();
    }

    public TimeTable getTimeTable(String identifier) {
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
            throw new RuntimeException("오류 발생", e);
        }
        
        return parseTimeTable(response.getBody());
    }

    private TimeTable parseTimeTable(String xml) {
        try {
            TableInfo tableInfo = xmlMapper.readValue(xml, TableInfo.class);
            return convert(tableInfo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    private TimeTable convert(TableInfo tableInfo) {
        TableParse parsedTable = tableInfo.getTables().get(0);

        TimeTable timeTable = new TimeTable();
        timeTable.setIdentifier(parsedTable.getIdentifier());
        timeTable.setYear(parsedTable.getYear());
        timeTable.setSemester(parsedTable.getSemester());

        for (SubjectParse subjectParse : parsedTable.getSubjects()) {
            Subject subject = setSubject(subjectParse);

            timeTable.addSubject(subject);
        }

        return timeTable;
    }

    private Subject setSubject(SubjectParse subjectParse) {
        Subject subject = new Subject();
        subject.setId(subjectParse.getId());
        subject.setName(subjectParse.getName());
        subject.setCode(subjectParse.getInternal());
        subject.setProfessor(subjectParse.getProfessor());
        for (ClassTime classTime : subjectParse.getClassTimes()) {
            SubjectTime subjectTime = setSubjectTime(classTime);

            subject.addSubjectTime(subjectTime);
        }
        return subject;
    }

    private SubjectTime setSubjectTime(ClassTime classTime) {
        SubjectTime subjectTime = new SubjectTime();
        subjectTime.setDay(classTime.getDay());
        subjectTime.setPlace(classTime.getPlace());
        subjectTime.setStartTime(classTime.getStartTime());
        subjectTime.setEndTime(classTime.getEndTime());
        return subjectTime;
    }

}
