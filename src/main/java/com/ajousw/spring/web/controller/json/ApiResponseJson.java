package com.ajousw.spring.web.controller.json;

import com.ajousw.spring.domain.auth.jwt.token.TokenStatusCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@ToString
@Getter
public class ApiResponseJson {
    public HttpStatus httpStatus;
    public int code;
    public Object data;

    public ApiResponseJson(HttpStatus httpStatus, int code, Object data) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.data = data;
    }

    public ApiResponseJson(HttpStatus httpStatus, Object data) {
        this.httpStatus = httpStatus;
        this.code = TokenStatusCode.OK;
        this.data = data;
    }
}

