package org.codenova.craft.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/*
    주문 상세 조회 시 프론트에 전달할 주문 항목 요약 정보
 */

@Setter
@Getter
@Builder
public class OrderItemSummary {
    private Long id;              // 주문 항목 ID

    private String productId;     // 제품 ID

    private String productName;   // 제품 이름

    private Integer quantity;     // 주문 수량

    private String status;        // 주문 상태
}
