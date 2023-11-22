package com.ajousw.spring.domain.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("select s from Subject s left join fetch s.subjectTimes where s.timeTable=:timeTable")
    List<Subject> findAllByTimeTableFetchSubjectTimes(@Param("timeTable") TimeTable timeTable);
}
