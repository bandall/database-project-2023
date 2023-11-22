package com.ajousw.spring.domain.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("select m from Member m left join fetch m.timeTable where m.email=:email")
    Optional<Member> findByEmailFetchTimeTable(@Param("email") String email);
}
