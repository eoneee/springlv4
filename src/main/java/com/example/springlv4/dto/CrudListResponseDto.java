package com.example.springlv4.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.*;

@Getter
public class CrudListResponseDto extends MsgResponseDto {
    //extends : 상속 (다중상속을 지원하지 않음) - 클래스 확정
    //implements : 상속 (다중상속 지원, 부모의 메소드를 오버라이딩해야함) - 인터페이스 구현
    List<CommentResponseDto> crudList = new ArrayList<>();
    //CommentResponseDto를 List화 해줌 : 댓글 리스트로 만들어 놓기
    public CrudListResponseDto(String msg, int statusCode){
        super(msg, statusCode);
        //부모클래스로부터 상속받은 필드나 메소드를 자식 클래스에서 참조하는데 사용하는 참조 변수
        //부모클래스의 멤버에 접근 가능
    }
    public void add(CommentResponseDto commentResponseDto){
        crudList.add(commentResponseDto);
    }
}
