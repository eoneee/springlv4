package com.example.springlv4.controller;

import com.example.springlv4.dto.LoginRequestDto;
import com.example.springlv4.dto.MsgResponseDto;
import com.example.springlv4.dto.SignupRequestDto;
import com.example.springlv4.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController //
//@Controller : Client로 부터 view반환,
//@RestController는 클래스의 각 메서드마다 @ResponseBody를 추가할 필요가 없어짐(@Controller와의 차이점)MVC Controller클래스 명시
@RequestMapping("/api/auth")
//기본 URL. 요청에 대해 어떤 Controller, 어떤메소드가 처리할지 알려줌
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/signup")
    //ResponseEntity: 결과값, 상태코드, 헤더값을 모두 프론트에 넘겨줄 수 있고, 에러코드 전송
    //@RequestBody: HTTP Method 안의 body 값을 Mapping(key:value 로 짝지어줌)
    public ResponseEntity<MsgResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        //ResponseEntity - 상태코드와 Json변환객체를 지정해줌
        //MsgResponseDto로 상태코드 설정
        //@Valid : 객체 안에 들어오는 값에 대한 검증 가능
        userService.signup(signupRequestDto);
        return ResponseEntity.ok(new MsgResponseDto("회원가입 완료", HttpStatus.OK.value()));
        //200(OK)응답 코드와 데이터를 생성
    }

    @ResponseBody
    //View페이지가 아닌 반환값 그대로 클라이언트한테 return하고 싶은 경우
    @PostMapping("/login")
    public ResponseEntity<MsgResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        //메세지와 함께 JWT토큰 반환 값 response해줌
        userService.login(loginRequestDto, response);
        return ResponseEntity.ok(new MsgResponseDto("로그인 완료",HttpStatus.OK.value()));
    }

    @PostMapping("/forbidden")
    public ModelAndView forbidden() {
        return new ModelAndView("forbidden");
    }

}