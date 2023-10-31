package com.ajousw.spring.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 로그 ID를 남기기 위한 로깅 필터 필터 최상단에 존재한다.
 */
@Slf4j
@Component
@Order(SecurityProperties.DEFAULT_FILTER_ORDER - 2)
public class LogFilter extends OncePerRequestFilter {
    public static final String TRACE_ID = "traceId";
    public static final String[] noFilterUrl = {"/error", "/favicon.ico"};

    // -Djava.net.preferIPv4Stack=true
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString().substring(24, 36);

        if (PatternMatchUtils.simpleMatch(noFilterUrl, requestURI)) {
            filterChain.doFilter(request, response);
            MDC.clear();
            return;
        }

        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        MDC.put(TRACE_ID, uuid);
        long startTime = System.currentTimeMillis();
        log.info("[REQUEST URI : {}, METHOD : {}, IP : {}]", requestURI, request.getMethod(), ip);

        filterChain.doFilter(request, response);
        long totalTime = System.currentTimeMillis() - startTime;
        log.info("Response Time = {}ms", totalTime);
        MDC.clear();
    }
}
