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
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    private Integer quantity;

    private String status;

    // 엔티티가 처음 저장되기 전에 자동 실행되는 메서드
    @PrePersist
    protected void prePersist() {
        status = "CREATED";

    }
}
