package com.ajousw.spring.domain.timetable.repository;

import com.ajousw.spring.domain.member.repository.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    Optional<TimeTable> findByMember(Member member);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteByMember(Member member);
}
