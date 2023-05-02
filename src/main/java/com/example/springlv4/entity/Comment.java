package com.example.springlv4.entity;


import com.example.springlv4.dto.CommentRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Id 숫자를 자동으로 만들어 줌
    @Column(name = "commentId")
    //CommentId라고 이름을 붙여서 사용할 것 : DB에서 쓰는 Comment_Id로 쓰는 것이 좋음
    private long id;

    @Column(nullable = false)
    //nullable = false : 데이터베이스에 명시적으로 not null 제약을 걸어주지만 유효성 검사는 해주지 않음
    private String content;


    //////// Users 테이블과 연관관계 //////
    @ManyToOne
    //Many가 주인
    //Comment : User(다대일)
    @JoinColumn(name = "userId", nullable = false)
    //Users의 UserId를 가져와서 JoinColumn 해줄 것
    private User user;
    ///////////////////////////////////




    //////// Crud 테이블과 연관관계 ////////
    @ManyToOne(fetch = FetchType.LAZY)
    //Comment : Crud(다대일)
    @JoinColumn(name = "crudId", nullable = false)
    //Crud에서 crudId를 가져와서 JoinColumn 해줄 것
    @JsonBackReference
    //순환참조 막아줌
    //자식 class - @JsonBackReference
    private Crud crud;
    ///////////////////////////////////



    public Comment(CommentRequestDto commentRequestDto){
        this.content = commentRequestDto.getContent();
        //commentRequestDto에서 request된 content를 생성해줌
    }


    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
        //commentRequestDto에서 request된 content를 생성해줌
    }

    public void addUser(User user){
        this.user = user;
        //users를 가져와서 생성해줌
    }

    public void addCrud(Crud crud){
        this.crud = crud;
        //crud를 가져와서 생성해줌
    }
}