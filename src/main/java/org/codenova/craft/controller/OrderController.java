package org.codenova.craft.controller;


import lombok.RequiredArgsConstructor;

import org.codenova.craft.entity.*;
import org.codenova.craft.repository.*;
import org.codenova.craft.request.NewOrder;
import org.codenova.craft.response.Demand;
import org.codenova.craft.response.OrderItemSummary;
import org.codenova.craft.service.BomService;
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
    private final InventoryRepository inventoryRepository;
    private final PurchaseRepository purchaseRepository;
    private final BomService bomService;


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


    // 등록된 주문정보 리스트 조회 API
    @GetMapping("/api/order")
    public ResponseEntity<?> getAllOrders() {

        // DB에서 모든 주문 데이터 조회
        List<Order> orders = orderRepository.findAll();

        // 응답 데이터를 담을 Map 생성
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("orders", orders);

        return ResponseEntity.status(200).body(response);

    }

    // 주문상세 정보 조회 API
    @GetMapping("/api/order/{orderId}")
    public ResponseEntity<?> getAllOrders(@PathVariable Long orderId) {

        /*
            findById는 Optional로 반환되므로 실제 실제 프로젝트 때는
            실제 실제 프로젝트 때는 이 부분 분기처리 해야 함!
         */

        // DB에서 orderId에 해당하는 주문 조회 (없는 경우 예외 발생)
        Order order = orderRepository.findById(orderId).orElseThrow();

        // 해당 주문에 포함된 모든 주문 항목들 조회
        List<OrderItem> items = orderItemRepository.findByOrder(order);

        // 주문 항목들을 프론트에 전달할 수 있도록 요약 정보로 가공
        List<OrderItemSummary> itemsSummary = items.stream().map((elm) -> {
            return OrderItemSummary.builder()
                    .id(elm.getId())                            // 주문 항목 ID
                    .productId(elm.getProduct().getId())        // 제품 ID
                    .productName(elm.getProduct().getName())    // 제품 이름
                    .quantity(elm.getQuantity())                // 주문 수량
                    .status(elm.getStatus())                    // 상태
                    .build();
        }).toList();

        // 클라이언트에 보낼 응답 데이터 구성
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);              // 응답 상태 코드
        response.put("order", order);             // 주문 전체 정보
        response.put("orderItems", itemsSummary); // 주문 항목 요약 리스트

        return ResponseEntity.status(200).body(response);
    }


    // 주문 확정 처리 API
    @PatchMapping("/api/order/{orderId}/approve")
    public ResponseEntity<?> approveOrder(@PathVariable Long orderId) {

        // 주문 ID로 주문 조회
        Order order = orderRepository.findById(orderId).orElseThrow();

        // 해당 주문의 주문 항목 전체 조회
        List<OrderItem> orderItemList = orderItemRepository.findByOrder(order);

        // 각 주문 항목 처리
        for (OrderItem orderItem : orderItemList) {

            // 해당 제품의 재고 정보 조회
            Inventory inventory = inventoryRepository.findByProduct(orderItem.getProduct());

            // 실제 사용 가능한 재고 계산(전체 재고 - 이미 예약된 수량)
            if (inventory.getStockQuantity() - inventory.getReservedQuantity() >= orderItem.getQuantity()) {
                orderItem.setStatus("COMPLETED");
                orderItemRepository.save(orderItem);

                inventory.setReservedQuantity(inventory.getReservedQuantity() + orderItem.getQuantity());
                inventoryRepository.save(inventory);

                // 재고가 부족한 경우
            } else {
                // 필요한 자재 목록 계산
                List<Demand> demands = bomService.calculateRequiredMaterials(orderItem.getProduct());

                // 주문 항목 상태 변경
                orderItem.setStatus("WAITING");
                orderItemRepository.save(orderItem);

                // 자재별로 반복하며 부족한 수량만큼 구매 요청 생성
                for (Demand demand : demands) {
                    Product material = demand.getProduct();
                    int requiredQty = demand.getQuantity();



                    Purchase purchase = Purchase.builder().build();


                    purchaseRepository.save(purchase);
                }
            }

            return null;
        }
    }
}
