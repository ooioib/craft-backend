package org.codenova.craft.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;

    private String status;

    private Long sourceOrderId;

    private Long sourceOrderItemId;

    private LocalDateTime createdAt;

    private LocalDate expectedAt;

}
