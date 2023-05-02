package com.example.springlv4.dto;


import com.example.springlv4.entity.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//@AllArgsConstructor : 해당객체 내에 있는 모든 변수들을 인수로 받는 생성자를 만들어내는 어노테이션
public class ErrorResponseDto {
    private int statusCode;
    private String error;
    private String msg;

    public ErrorResponseDto(StatusEnum statusEnum){
        //StatusEnum : 에러 종류, 이름, 코드 지정
        this.statusCode= statusEnum.getStatus().value();
        this.error = statusEnum.getStatus().name();
        this.msg=statusEnum.getMsg();
    }
}
