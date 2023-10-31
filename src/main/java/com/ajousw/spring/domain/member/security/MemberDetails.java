package com.ajousw.spring.domain.member.security;

import com.ajousw.spring.domain.member.enums.Role;
import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
public class MemberDetails implements UserDetails {
    private Long id;
    private String email;
    private String username;
    private String password;
    private Role role;
    private boolean isEmailVerified;
    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private Collection<? extends GrantedAuthority> authorities;

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }
}