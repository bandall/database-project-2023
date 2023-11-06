package com.ajousw.spring.domain.tablejoin;

import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.member.repository.MemberRepository;
import com.ajousw.spring.domain.timetable.repository.Subject;
import com.ajousw.spring.domain.timetable.repository.SubjectRepository;
import com.ajousw.spring.web.controller.dto.timetable.CommonEmptyTimeDto;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TableJoinService {

    public static final int DAY_OF_WEEK = 7;
    public static final int MAX_DAY_TIME = 289;
    private final SubjectRepository subjectRepository;
    private final MemberRepository memberRepository;

    public Map<Day, List<String>> findCommonEmptyTime(CommonEmptyTimeDto commonEmptyTimeDto) {
        List<Member> members = getMembers(commonEmptyTimeDto.getEmails());

        byte[][] emptyTimeTable = new byte[DAY_OF_WEEK][MAX_DAY_TIME];
        for (Member member : members) {
            setEmptyTime(emptyTimeTable, member);
        }

        return formatCommonEmptyTime(emptyTimeTable, commonEmptyTimeDto);
    }

    private List<Member> getMembers(List<String> ownerEmails) {
        return ownerEmails.stream()
                .map(this::getMemberByEmail).toList();
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다. " + email)
        );
    }

    private void setEmptyTime(byte[][] emptyTimeTable, Member member) {
        List<Subject> subjects = getSubjects(member);

        for (Subject subject : subjects) {
            subject.getSubjectTimes().forEach((s) -> {
                setBusyTime(s.getDay(), s.getStartTime(), s.getEndTime(), emptyTimeTable);
            });
        }
    }

    private List<Subject> getSubjects(Member member) {
        List<Subject> subjects = subjectRepository.findAllByTimeTableFetch(
                member.getTimeTable().getTableId());
        return subjects == null ? Collections.emptyList() : subjects;
    }

    private void setBusyTime(int day, int startTime, int endTime, byte[][] emptyTimeTable) {
        for (int i = startTime; i < endTime; i++) {
            emptyTimeTable[day][i] = 1;
        }
    }

    private Map<Day, List<String>> formatCommonEmptyTime(byte[][] emptyTimeTable,
                                                         CommonEmptyTimeDto commonEmptyTimeDto) {
        Map<Day, List<String>> emptyTimes = new LinkedHashMap<>();
        for (int i = 0; i < DAY_OF_WEEK; i++) {
            emptyTimes.put(Day.fromValue(i), printDay(emptyTimeTable[i], commonEmptyTimeDto));
        }
        return emptyTimes;
    }

    private List<String> printDay(byte[] emptyDayTime, CommonEmptyTimeDto commonEmptyTimeDto) {
        List<String> emptyTime = new ArrayList<>();

        int start = -1;
        int startTime = commonEmptyTimeDto.getStartTime();
        for (int i = startTime; i < MAX_DAY_TIME; i++) {
            if (emptyDayTime[i] == 0 && (i == startTime || emptyDayTime[i - 1] == 1)) {
                start = i;
            }

            if (start != -1 && (emptyDayTime[i] == 1 || i == emptyDayTime.length - 1)) {
                int hoursStart = (start * 5) / 60;
                int minutesStart = (start * 5) % 60;
                int hoursEnd = (i * 5) / 60;
                int minutesEnd = (i * 5) % 60;

                int timeDiff = ((hoursEnd * 60 + minutesEnd) - (hoursStart * 60 + minutesStart));
                if (timeDiff >= commonEmptyTimeDto.getMinTimeDiff()) {
                    String formattedTime = String.format("빈 시간대: %02d:%02d ~ %02d:%02d", hoursStart, minutesStart,
                            hoursEnd,
                            minutesEnd);
                    emptyTime.add(formattedTime);
                }
                start = -1;
            }
        }
        return emptyTime;
    }
}
