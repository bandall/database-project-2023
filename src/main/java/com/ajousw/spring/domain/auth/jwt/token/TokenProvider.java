package com.ajousw.spring.domain.auth.jwt.token;

import com.ajousw.spring.domain.auth.jwt.redis.RedisAccessTokenBlackListRepository;
import com.ajousw.spring.domain.member.security.UserPrinciple;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Slf4j
public class TokenProvider {

    protected static final String KEY_AUTHORITIES = "auth";
    protected static final String KEY_TOKEN_ID = "tokenId";
    protected static final String KEY_USERNAME = "username";

    protected final String secrete;
    protected final Key hashKey;

    protected final RedisAccessTokenBlackListRepository blackListRepository;

    public TokenProvider(String secrete, RedisAccessTokenBlackListRepository blackListRepository) {
        this.secrete = secrete;
        byte[] keyBytes = Decoders.BASE64.decode(secrete);
        this.hashKey = Keys.hmacShaKeyFor(keyBytes);
        this.blackListRepository = blackListRepository;
    }

    // access 토큰을 인자로 전달받아 클레임을 만들어 권한 정보 반환
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(hashKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(KEY_AUTHORITIES).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 커스텀한 UserPrinciple 객체 사용 -> 이후 추가적인 데이터를 토큰에 넣을 경우 UserPrinciple 객체 및 이 클래스의 함수들 수정 필요
        UserPrinciple principle = new UserPrinciple(claims.getSubject(), claims.get(KEY_USERNAME, String.class),
                authorities);

        return new UsernamePasswordAuthenticationToken(principle, token, authorities);
    }

    // 토큰 유효성 검사 -> access, refresh 토큰 둘 다 검증하는 함수이므로 access 토큰 블랙리스트는 체크하지 않는다.
    public TokenValidationResult validateToken(String token) {
        TokenValidationResult validResult = new TokenValidationResult(false, TokenType.ACCESS, null, null, null);
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder().setSigningKey(hashKey).build().parseClaimsJws(token).getBody();

            // 토큰에 권한정보가 없을 경우 Refresh 토큰
            if (claims.get(KEY_AUTHORITIES) == null) {
                validResult.setTokenType(TokenType.REFRESH);
            }

            validResult.setResult(true);
            validResult.setTokenId(claims.get(KEY_TOKEN_ID, String.class));
            validResult.setTokenStatus(TokenStatus.TOKEN_VALID);
            return validResult;
        } catch (ExpiredJwtException e) {
            // 만료된 토큰의 경우에도 tokenId를 부여할 수 있게 수정
            log.info("만료된 jwt 토큰");
            claims = e.getClaims();
            validResult.setTokenStatus(TokenStatus.TOKEN_EXPIRED);
            validResult.setTokenId(claims.get(KEY_TOKEN_ID, String.class));
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 jwt 서명");
            validResult.setTokenStatus(TokenStatus.TOKEN_WRONG_SIGNATURE);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 jwt 서명");
            validResult.setTokenStatus(TokenStatus.TOKEN_HASH_NOT_SUPPORTED);
        } catch (IllegalArgumentException e) {
            log.info("잘못된 jwt 토큰");
            validResult.setTokenStatus(TokenStatus.TOKEN_WRONG_SIGNATURE);
        }

        return validResult;
    }

    public boolean isAccessTokenBlackList(String accessToken) {
        if (blackListRepository.isKeyBlackList(accessToken)) {
            log.info("폐기된 jwt 토큰");
            return true;
        } else {
            return false;
        }
    }
}
