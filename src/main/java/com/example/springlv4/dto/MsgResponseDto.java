package com.example.springlv4.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;

//모든 필드값을 파라미터로 받는 생성자 생성
@Getter
@AllArgsConstructor
//AllArgsConstructor : 모든 필드 값을 파라미터로 받는 생성자를 만듦
@NoArgsConstructor
//@NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성
@Builder
//@Builder : 해당 클래스에 자동으로 빌더추가
public class MsgResponseDto {
    private String msg;
    private int statusCode;


}
