package org.codenova.craft.repository;

import org.codenova.craft.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 주문 상태(status)에 따라 주문 리스트를 조회
    List<Order> findByStatus(String status);
}
