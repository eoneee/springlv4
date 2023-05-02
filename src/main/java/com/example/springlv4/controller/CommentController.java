package com.example.springlv4.controller;

import com.example.springlv4.dto.*;
import com.example.springlv4.service.CommentService;
import com.example.springlv4.service.CrudService;
import com.example.springlv4.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
//Json형태의 객체반환
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/comment")
    //@requestBody : json기반의 메세지를 사용하는 경우 http요청의 본문(body)이 그대로 전달 됨
    //@HttpServletRequest : Service등과 같은 메서드에 파라미터로 전달해줌, 서블릿페이지가 실행되는 동안에만 메모리에 존재하는 개체
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(requestDto, request);
    }

    //댓글 수정
    //@PathVariable : URL변수를 받아서 사용
    @PutMapping("/comment/{id}")
    public ResponseEntity updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.updateComment(id, requestDto, request);
    }

    //댓글 삭제
    @DeleteMapping("/comment/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id, HttpServletRequest request) {
        return commentService.deleteComment(id, request);
    }
}







/*
package com.example.springlv3.controller;

import com.example.springlv3.dto.*;
import com.example.springlv3.service.CommentService;
import com.example.springlv3.service.CrudService;
import com.example.springlv3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

//Json형태의 객체반환
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/comment")
    public CommentResponseDto createBoard(@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(requestDto, request);
    }

    //댓글 수정
    @PutMapping("/comment/{id}")
    public ResponseEntity<CommentResponseDto> updateBoard(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.updateComment(id, requestDto, request);
   }

    //댓글 삭제
    @DeleteMapping("/comment/{id}")
   public MsgResponseDto deleteBoard(@PathVariable Long id, HttpServletRequest request) {
       return commentService.deleteComment(id, request);
    }
}
*/
