package com.ajousw.spring.domain.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubjectTimeRepository extends JpaRepository<SubjectTime, Long> {

    @Query("delete from SubjectTime st where st.subject=:subject")
    void deleteBySubject(@Param("subject") Subject subject);
}
