package com.ajousw.spring.domain.timetable.everytime;

import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.timetable.parser.ClassTime;
import com.ajousw.spring.domain.timetable.parser.SubjectParse;
import com.ajousw.spring.domain.timetable.parser.TableInfo;
import com.ajousw.spring.domain.timetable.parser.TableParse;
import com.ajousw.spring.domain.timetable.repository.Subject;
import com.ajousw.spring.domain.timetable.repository.SubjectTime;
import com.ajousw.spring.domain.timetable.repository.TimeTable;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class EveryTimeParser {
    private final XmlMapper xmlMapper = new XmlMapper();

    public TableInfo parseTimeTable(String xml) {
        try {
            return xmlMapper.readValue(xml, TableInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public TimeTable getTimeTable(TableInfo tableInfo, Member member) {
        TableParse parsedTable = tableInfo.getTables().get(0);

        return TimeTable.builder()
                .member(member)
                .identifier(parsedTable.getIdentifier())
                .year(parsedTable.getYear())
                .semester(parsedTable.getSemester())
                .build();
    }

    public List<Subject> getSubject(TableInfo tableInfo, TimeTable timeTable) {
        TableParse parsedTable = tableInfo.getTables().get(0);
        List<SubjectParse> subjectParses = parsedTable.getSubjects();

        return subjectParses.stream()
                .map(s -> setSubject(s, timeTable))
                .toList();
    }

    private Subject setSubject(SubjectParse subjectParse, TimeTable timeTable) {
        Subject subject = Subject.builder()
                .timeTable(timeTable)
                .everyTimeSubjectId(subjectParse.getId())
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
        return SubjectTime.builder()
                .subject(subject)
                .day(classTime.getDay())
                .place(classTime.getPlace())
                .startTime(classTime.getStartTime())
                .endTime(classTime.getEndTime())
                .build();
    }
}
