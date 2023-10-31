package com.ajousw.spring.domain.auth;

import com.ajousw.spring.domain.member.repository.MemberJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final MemberJpaRepository memberJpaRepository;

}
