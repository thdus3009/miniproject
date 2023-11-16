package com.sy.miniproject.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 해당 어노테이션을 쓰면 다른 클래스에서 이 클래스를 상속받을 때 아래 필드들도 같이 칼럼으로 인식한다.
@EntityListeners(AuditingEntityListener.class) // 해당 클래스에 Audulting 기능 포함 시킨다.
public class BaseTimeEntity {
    // Entity들이 시간을 자동으로 관리하는 역할
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;


}
