package com.ajousw.spring.domain.member;

import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberJpaRepository memberJpaRepository;

    public Member getMember(String email) {
        Member member = memberJpaRepository.findByEmail(email).orElseThrow();
        return member;
    }
}
