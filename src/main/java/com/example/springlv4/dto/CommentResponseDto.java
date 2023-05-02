package com.example.springlv4.dto;


import com.example.springlv4.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
//받아온 데이터들을 교환 해줌
//@Getter : 접근자생성
@NoArgsConstructor
public class CommentResponseDto{
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;


    public  CommentResponseDto(Comment comment) {
        //Comment Entity에서 데이터들을 가져옴
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username =comment.getUser().getUsername();
        //Comment의 User테이블을 가져오고 거기서 username을 가져옴
        this.createAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
