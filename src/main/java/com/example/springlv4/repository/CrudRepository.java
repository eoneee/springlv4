package com.example.springlv4.repository;

import com.example.springlv4.entity.Crud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
//spring data JPA
public interface CrudRepository extends JpaRepository<Crud, Long> {
    //내림차순 정렬
    Collection<Crud> findAllByOrderByModifiedAtDesc();
    List<Crud> findAllByOrderByCreatedAtDesc();
    //Crud List로 모든 값을 내림차순정렬하여 가지고 옴

    Optional<Crud> findByIdAndUserId(Long id, Long userId);
}
