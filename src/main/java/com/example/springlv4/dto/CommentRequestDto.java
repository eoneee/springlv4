package com.example.springlv4.dto;

import lombok.Getter;

@Getter
// 받아온 데이터들을 교환 해줌
//@Getter : 접근자생성
public class CommentRequestDto {
    private Long crudId;
    private String content;

}
