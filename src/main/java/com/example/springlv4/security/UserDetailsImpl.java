package com.example.springlv4.security;
import com.example.springlv4.entity.User;
import com.example.springlv4.entity.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    //사용자가 존재하지 않는경우를 제외하고 UserDetailsServiceImpl에서 리턴해주는 user와 username을 받음
    //UserDetails : 인증된 사용자 정보를 제공 하기 위한 Interface
    //UserDetailsImpl : UserDetails의 구현체가 됨 -> 리스트로 저장을 해놓음

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
        //GrantedAuthority는 ID, PW기반 인증에서 UserDetailsSericeImpl을 통해 조회됨
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();
        //UserRoleEnum에서 권한 가져옴

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        //사용자의 role과 권한을 조회하여 simpleGrantedAuthority목륵을 autorities에 세팅

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