package org.codenova.craft.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class NewOrder {

    @Setter
    @Getter
    public static class Item {
        private String productId;
        private int quantity;
    }

    private LocalDate dueDate;
    private List<Item> items;
}
