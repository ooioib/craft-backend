package org.codenova.craft.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne // 하나의 Inventory는 하나의 Product와 1:1 관계
    private Product product;

    private Integer stockQuantity;

    private Integer reservedQuantity;

    private LocalDateTime updatedAt;

    // 엔티티가 업데이트되기 직전에 실행되는 메서드
    @PreUpdate
    public void preUpdate() {
        // 업데이트 직전에 현재 시간으로 갱신
        updatedAt = LocalDateTime.now();
    }
}
