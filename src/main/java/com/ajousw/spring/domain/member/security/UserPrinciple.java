package com.ajousw.spring.domain.member.security;


import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class UserPrinciple extends User {

    private String email;

    // Access의 유저 정보를 컨트롤러에 유저 정보를 전달하기 위한 커스텀 객체, password는 jwt 토큰에 존재하지 않아 전달하지 않는다.
    // 토큰에 정보를 추가하기 위해서는 추가 필드를 만들어 준다.
    public UserPrinciple(String email, String username, Collection<? extends GrantedAuthority> authorities) {
        super(username, "[PASSWORD_ERASED]", authorities);
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserPrinciple(" +
                "email=" + email +
                " username=" + getUsername() +
                " role=" + getAuthorities() +
                ')';
    }
}
