package com.ajousw.spring.domain.timetable.repository;

import com.ajousw.spring.domain.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    @Query("select tt from TimeTable tt join fetch tt.subjectList s")
    Optional<TimeTable> findByMember(Member member);

    void deleteByMember(Member member);
}
