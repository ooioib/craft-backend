package org.codenova.craft.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.codenova.craft.entity.Product;

@Setter
@Getter
@Builder
public class Demand {
    private Product product;
    private Integer quantity;
}
