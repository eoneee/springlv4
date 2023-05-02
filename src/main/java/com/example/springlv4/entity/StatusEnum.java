package com.example.springlv4.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    // 200
    OK(HttpStatus.OK, "OK"),


    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST"),

    TOKEN_ERROR(HttpStatus.BAD_REQUEST, "토큰 에러"),


    // 404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 없습니다"),

    NOT_AUTHENTICATION(HttpStatus.NOT_FOUND, "사용자만 게시글을 삭제할 수 있습니다."),

    NOT_EXIST_CRUD(HttpStatus.NOT_FOUND, "글이 존재하지 않습니다."),
    NOT_EXIST_COMMENT(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."),

    TOKEN_NULL(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND"),


    // 500
    INTERNAL_SERER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");

    private final HttpStatus status;
    private final String msg;
}