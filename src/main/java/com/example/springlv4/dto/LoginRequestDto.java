package com.example.springlv4.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
//setter 메소드를 주입시킴, 생성자를 만들어줌
@Getter
public class LoginRequestDto {
    private String username;
    private String password;
}