package org.codenova.craft.repository;

import org.codenova.craft.entity.Inventory;
import org.codenova.craft.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

   // 특정 제품(Product)에 해당하는 재고 정보를 조회
   Inventory findByProduct(Product product);
}
