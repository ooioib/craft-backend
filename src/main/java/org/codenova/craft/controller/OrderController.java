package org.codenova.craft.controller;


import lombok.RequiredArgsConstructor;

import org.codenova.craft.entity.Order;
import org.codenova.craft.entity.OrderItem;
import org.codenova.craft.repository.OrderItemRepository;
import org.codenova.craft.repository.OrderRepository;
import org.codenova.craft.repository.ProductRepository;
import org.codenova.craft.request.NewOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController       // REST API 컨트롤러
@RequiredArgsConstructor // 의존성 자동 주입
public class OrderController {
    private final OrderRepository orderRepository;         // 주문 정보 저장
    private final OrderItemRepository orderItemRepository; // 주문 항목 저장
    private final ProductRepository productRepository;     // 제품 정보 조회


    // 주문 처리하는 POST API
    @PostMapping("/api/order/new")
    public ResponseEntity<?> newOrderHandle(@RequestBody NewOrder newOrder) {

        // 클라이언트로부터 받은 NewOrder 객체에서 납기일을 꺼내서 주문 객체 생성
        Order order = Order.builder()
                .dueDate(newOrder.getDueDate()) // 납기일을 order에 설정
                .build(); // Order 객체 생성

        // 주문 정보를 DB에 저장
        orderRepository.save(order);

        // 주문 항목을 담을 리스트 생성
        List<OrderItem> orderItems = new ArrayList<>();

        // 각 주문 항목을 하나씩 추출해서 반복처리
        for (NewOrder.Item item : newOrder.getItems()) {
            String productId = item.getProductId();  // 제품 ID 추출
            int quantity = item.getQuantity();       // 수량 추출

            // 제품 정보를 DB에서 조회해서 주문 항목 객체 생성
            OrderItem orderItem = OrderItem.builder()
                    .order(order) // 현재 주문과 연관 설정
                    .product(productRepository.findById(productId).get()) // 제품 ID로 DB에서 제품 조회 후 설정
                    .quantity(quantity) // 주문한 수량 설정
                    .build(); // OrderItem 객체 생성

            // 리스트에 주문 항목 추가
            orderItems.add(orderItem);
        }

        // 모든 주문 항목을 저장
        orderItemRepository.saveAll(orderItems);

        // 응답 데이터 구성
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);                         // 상태 코드
        response.put("message", "successfully added order"); // 성공 메시지
        response.put("order", order);                        // 저장된 주문 객체 반환

        return ResponseEntity.status(200).body(response);
    }


    // 현재 등록된 주문정보 리스트 조회 API
    @GetMapping("/api/order/list")
    public ResponseEntity<?> orderListHandle() {

        // DB에서 모든 주문 데이터 조회
        List<Order> orders = orderRepository.findAll();

        // 응답 데이터를 담을 Map 생성
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("orders", orders);

        return ResponseEntity.status(200).body(response);
    }

    // 특정 주문상세 정보



}
