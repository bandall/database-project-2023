package com.ajousw.spring.domain.timetable.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("select s from Subject s where s.everyTimeSubjectId in :everyTimeSubjectId")
    List<Subject> findAllByEveryTimeSubjectIdIs(@Param("everyTimeSubjectId") List<Long> everyTimeSubjectId);

    @Query("select s from Subject s left join fetch s.subjectTimes where s.subjectId in :subjectIds")
    List<Subject> findAllBySubjectIdIsFetchSubjectTime(@Param("subjectIds") List<Long> subjectIds);

    @Query("select s.subjectId from Subject s "
            + "where not exists ("
            + "    select 1"
            + "    from TimeTableSubject ts"
            + "    where ts.subject = s"
            + ")")
    List<Long> findAllSubjectIdWhereNonReferenced();

    @Modifying
    @Query("delete from Subject s where s.subjectId in :subjectIds")
    void deleteAllByIds(@Param("subjectIds") List<Long> subjectIds);
}
