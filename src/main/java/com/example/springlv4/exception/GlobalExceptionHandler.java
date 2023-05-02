package com.example.springlv4.exception;

import com.example.springlv4.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    //@ExceptionHandler : Bean내에서 발생하는 예외를 잡아서 하나의 메서드에서 처리해주는 기능
    protected ResponseEntity<ErrorResponseDto> handleCustomException(final CustomException e){
        log.error("handleCustomException : {}", e.getStatusEnum());
        return ResponseEntity
                .status(e.getStatusEnum().getStatus().value())
                .body(new ErrorResponseDto(e.getStatusEnum()));
    }

}