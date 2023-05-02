package com.example.springlv4.repository;


import com.example.springlv4.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
//    List<Comment> findByCrud(Crud crud);
//    Optional<Comment> findByUserAndCrud(Users users, Crud crud);
//
//        List<Comment> findByCrud_IdOrderByModifiedAtDesc(Long id);
//        //Comment List를 Crud로 가져오기
//
//        List<Comment> findAllByOrderByCreatedAtDesc();
//        //Comment List를 모두 가져오기
}
