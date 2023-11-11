package com.ajousw.spring.domain.alarm.repository;

import com.ajousw.spring.domain.member.repository.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("select a from Alarm a join fetch a.subject where a.member.email=:email")
    List<Alarm> findByMemberEmailFetch(@Param("email") String email);

    @Query("select a from Alarm a join fetch a.subject where a.id=:id")
    Optional<Alarm> findByIdFetch(@Param("id") Long id);

    void deleteById(Long id);

    void deleteByMember(Member member);
}
