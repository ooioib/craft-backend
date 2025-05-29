package org.codenova.craft.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "`order`") // 테이블 이름이 예약어(order)이므로 백틱(`)으로 감싸서 사용
public class Order {

    @Id // 기본키(pk) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt;
    private LocalDate dueDate;
    private Integer priority;
    private String status;

    // 엔티티가 처음 저장되기 전에 자동 실행되는 메서드
    @PrePersist
    protected void prePersist() {
        createdAt = LocalDateTime.now();
        priority = 10;
        status = "CREATED";
    }
}