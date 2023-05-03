package com.example.springlv4.service;

import com.example.springlv4.dto.*;
import com.example.springlv4.entity.*;
import com.example.springlv4.exception.CustomException;
import com.example.springlv4.jwt.JwtUtil;
import com.example.springlv4.repository.CommentRepository;
import com.example.springlv4.repository.CrudRepository;
import com.example.springlv4.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrudService {

    private final CrudRepository crudRepository;
    private final CrudRequestDto crudRequestDto;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //글 생성하기
    @Transactional
    public CrudResponseDto createCrud(CrudRequestDto requestDto, User user) {
        Crud crud = new Crud(requestDto);
        crud.addUser(user);
        crudRepository.save(crud);
        return new CrudResponseDto(crud);
    }


    //글 전체 조회
    @Transactional(readOnly = true)
    //JPA 사용시, 변경감지 작업을 수행하지 않아 성능 이점
    public List<CrudResponseDto> getCrudList() {
        // 데이터 베이스에 저장 된 전체 게시물 전부다 가져오는 API
        // 테이블에 저장 되어 있는 모든 게시물 목록 조회
        //List<Crud> crudList = crudRepository.findAllByOrderByCreatedAtDesc();
        // DB 에서 가져온 것
        //return crudList.stream().map(CrudResponseDto::new).collect(Collectors.toList());

        return crudRepository.findAllByOrderByCreatedAtDesc().stream().map(CrudResponseDto::new).collect(Collectors.toList());
    }



    //전체목록 말고 하나씩 보기
    @Transactional(readOnly = true)
    public CrudResponseDto getCrud(Long id) {
        //조회하기 위해 받아온 crud의 id를 사용해서 해당 crud인스턴스가 테이블에 존재 하는지 확인하고 가져오기
        //Crud crud = table.get(id);->repository한테서 id를 가져오면 됨
        Crud crud = checkCrud(id);
        return new CrudResponseDto(crud);
    }

    //선택한 게시글 수정
    @Transactional
    //Long id: 담을 데이터,
    //HttpServletRequest request 객체: 로그인 토큰을 담고 있음
    public CrudResponseDto updateCrud(Long id, CrudRequestDto requestDto, User user) {
        Crud crud;
        //user 의 권한이 ADMIN 와 같다면,
        if(user.getRole().equals(UserRoleEnum.ADMIN)) {
            crud = crudRepository.findById(id).orElseThrow(
                    //글이 id로 찾아서 존재하지 않을 때 예외처리
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
            );
        //user 의 권한이 ADMIN 이 아니라면, 아이디가 같은 유저만 수정 가능
        } else {
            crud = crudRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("권한이 없습니다.")
            );
        }

        crud.update(requestDto);
        return new CrudResponseDto(crud);

    }

    //선택한 게시글 삭제
    @Transactional
    //MsgResponseDto 반환 타입, deleteCrud 메소드 명
    public void deleteCrud (Long id, User user) {
        Crud crud;
        //user 의 권한이 ADMIN 와 같다면,
        if(user.getRole().equals(UserRoleEnum.ADMIN)) {
            crud = crudRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
            );

        } else {
            //user 의 권한이 ADMIN 이 아니라면, 아이디가 같은 유저만 수정 가능
            crud = crudRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("권한이 없습니다.")
            );
        }
        crudRepository.delete(crud);
    }


    //글 존재 여부 확인
    private Crud checkCrud(Long id) {
        Crud crud = crudRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusEnum.NOT_EXIST_CRUD)
        );
        return crud;
    }

    //권한 여부
    private void isCrudUsers(User user, Crud crud){
        if(!crud.getUser().getUsername().equals(user.getUsername()) && !user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new CustomException(StatusEnum.NOT_AUTHENTICATION);
        }
    }

    //권한 확인
    private User checkJwtToken(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        //header 토큰을 가져오기
        Claims claims;
        // 토큰 검증
        if(token == null){
            throw new CustomException(StatusEnum.TOKEN_NULL);
        }
        if (!jwtUtil.validateToken(token)) {
            throw new CustomException(StatusEnum.TOKEN_ERROR);
        }
        claims = jwtUtil.getUserInfoFromToken(token);

        return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 없습니다.")
        );
    }

}



