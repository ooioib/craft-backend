package org.codenova.craft.repository;

import org.codenova.craft.entity.Order;
import org.codenova.craft.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // 특정 주문(Order)에 속한 모든 주문 항목(OrderItem)을 조회
    List<OrderItem> findByOrder(Order order);

    // ID로 직접 조회도 가능
    // List<OrderItem> findByOrderId(Long orderId);
}
