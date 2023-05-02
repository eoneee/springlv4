package com.example.springlv4.security;
import com.example.springlv4.entity.User;
import com.example.springlv4.entity.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user;
    //인증 완료된 User 객체
    private final String username;
    //인증 완료된 User ID
    //인증 완료된 User PW

    //인증 완료된 User를 가져오는 Getter
    public UserDetailsImpl(User user, String username) {
        this.user = user;
        this.username = username;
    }

    //인증 완료 된 User를 가져오는 Getter
    public User getUser() {
        return user;
    }

    //사용자 권한 GrantedAuthority로 추상화 및 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    //사용자 ID Getter, PW Getter
    @Override
    public String getUsername() {
        return this.username;
    }

    //사용자 PW Getter
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}