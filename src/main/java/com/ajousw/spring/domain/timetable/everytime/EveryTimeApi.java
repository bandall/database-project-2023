package com.ajousw.spring.domain.timetable.everytime;

import com.ajousw.spring.domain.ErrorMessage;
import com.ajousw.spring.domain.timetable.exception.EverytimeParsingException;
import com.ajousw.spring.domain.timetable.parser.TableInfo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class EveryTimeApi {

    private final EveryTimeParser everyTimeParser;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder().build();
    }

    public TableInfo getTimeTable(String identifier) {
        String param = "?identifier=" + identifier + "&friendInfo=true";
        ResponseEntity<String> response;
        try {
            response = webClient.post()
                    .uri("https://api.everytime.kr/find/timetable/table/friend" + param)
                    .header("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36")
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new EverytimeParsingException(ErrorMessage.EVERYTIME_API_FAILED, e);
        }

        if (response == null) {
            throw new EverytimeParsingException(ErrorMessage.EVERYTIME_API_FAILED);
        }

        return everyTimeParser.parseTimeTable(response.getBody());
    }
}
