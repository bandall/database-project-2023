package com.ajousw.spring.domain.auth.jwt.token;

public enum TokenStatus {
    TOKEN_VALID,
    TOKEN_EXPIRED,
    TOKEN_IS_BLACKLIST,
    TOKEN_WRONG_SIGNATURE,
    TOKEN_HASH_NOT_SUPPORTED,
    TOKEN_ID_NOT_MATCH,
    TOKEN_VALIDATION_TRY_FAILED,
    WRONG_AUTH_HEADER
}
