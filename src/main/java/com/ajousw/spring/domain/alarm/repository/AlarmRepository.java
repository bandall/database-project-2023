package com.ajousw.spring.domain.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("select a from Alarm a join fetch a.subject where a.member.email=:email")
    List<Alarm> findByMemberEmailFetch(String email);

    @Query("select a from Alarm a join fetch a.subject where a.id=:id")
    Optional<Alarm> findByIdFetch(Long id);

    void deleteById(Long id);

}
