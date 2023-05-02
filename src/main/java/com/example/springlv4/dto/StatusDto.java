package com.example.springlv4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@Data : gtter, setter, RequiredArgsConstructor 등 합쳐놓은 세트
@AllArgsConstructor(staticName = "add")
//staticName이라는 옵션을 사용하여 생성자를 private으로 생성하고,private생성자를 감싸고 있는 static factory메소드를 추가할 수 있음
public class StatusDto<T> {

    private int statusCode; // 필드명을 statusCode로 변경
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL) // null일 경우 반환할 때 보이지 않게 한다.
    //@JsonInclude : json으로 변경시에 null로 들어오는 값을 보고 싶지 않을 때
    private T data;

    public static <T> StatusDto<T> setSuccess(int statusCode, String message, T data){
        return StatusDto.add(statusCode, message, data);
        //setSuccess인 경우 데이터를 함께 add
    }

    public static <T> StatusDto<T> setFail(int statusCode, String message){
        return StatusDto.add(statusCode, message, null);
        //setFail인 경우 데이터를 null로 add
    }

}