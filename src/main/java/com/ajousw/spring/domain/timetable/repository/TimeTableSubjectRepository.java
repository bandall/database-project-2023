package com.ajousw.spring.domain.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimeTableSubjectRepository extends JpaRepository<TimeTableSubject, Long> {

    @Modifying
    @Query("delete from TimeTableSubject ts where ts.timeTable=:timeTable")
    void deleteAllByTimeTable(@Param("timeTable") TimeTable timeTable);

    @Modifying
    @Query("delete from TimeTableSubject ts where ts.subject=:subject")
    void deleteBySubject(@Param("subject") Subject subject);

}
