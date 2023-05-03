package com.example.springlv4.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "Users")
public class User {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 또는 SEQUENCE) 또는 TABLE) 또는 AUTO)
    //기본 키 생성을 DB에 위임 (Mysql), DB 시퀀스를 사용해서 기본 키 할당 (ORACLE), 키 생성 테이블 사용 (모든 DB 사용 가능)(TABLE),
    // 선택된 DB에 따라 자동으로 전략 선택(AUTO:DB에 따라 전략을 JPA 가 자동으로 선택되므로, DB를 변경해도 코드를 수정할 필요 없음)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId")
    //userId라고 칭할 것
    private Long id;

    @Column(nullable = false, unique = true)
    // nullable: null 허용 여부
    // unique: 중복 허용 여부 (false 일때 중복 허용) true : 중복 안됨
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    //json에서 출력되지 않음
    private String password;

    @Column(nullable = false)
    //null은 안됨
    @Enumerated(value = EnumType.STRING)
    //Enum의 선언된 상수의 이름을 string으로 변환하여 DB에 주입
    private UserRoleEnum role;

    public User(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }



}