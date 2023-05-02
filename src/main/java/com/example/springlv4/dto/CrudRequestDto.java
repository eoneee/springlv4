package com.example.springlv4.dto;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
//직접 class를 bean으로 등록하기 위한 어노테이션
public class CrudRequestDto {
    //Crud 입력된 데이터
    private String title;
    private String content;
}
