package com.ajousw.spring.domain.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeTableSubjectRepository extends JpaRepository<TimeTableSubject, Long> {

    @Query("select ts from TimeTableSubject ts where ts.timeTable=:timeTable")
    List<TimeTableSubject> findAllByTimeTable(@Param("timeTable") TimeTable timeTable);

}
