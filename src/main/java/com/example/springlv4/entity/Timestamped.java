package com.example.springlv4.entity;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@Getter
@MappedSuperclass
//쌩성시간, 수정시간 모든 엔티티에 공통으로 가져가야 하는 상황에서 선언(매핑정보만 상속받는 슈퍼클래스) -> 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class)
//엔티티 변화를 감지하고 테이블을 조작함, 해당 클래스에 Auditing기능 포함 -> 시간에 대해서 자동으로 값을 넣어줌
public class Timestamped {
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}

