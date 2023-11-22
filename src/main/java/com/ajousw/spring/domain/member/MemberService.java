package com.ajousw.spring.domain.member;

import com.ajousw.spring.domain.member.repository.Member;
import com.ajousw.spring.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberJpaRepository;

    public Member getMember(String email) {
        return memberJpaRepository.findByEmail(email).orElseThrow();
    }
}
