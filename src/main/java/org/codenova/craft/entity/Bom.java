package org.codenova.craft.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product parentProduct;

    @ManyToOne
    @JoinColumn(name = "child_product_id")
    private Product childProduct;

    private Integer quantity;
}
