package org.codenova.craft.service;

import lombok.AllArgsConstructor;
import org.codenova.craft.entity.Bom;
import org.codenova.craft.entity.Product;
import org.codenova.craft.repository.BomRepository;
import org.codenova.craft.response.BomNode;
import org.codenova.craft.response.Demand;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class BomService {
    private final BomRepository bomRepository;

    // 특정 제품을 기준으로 BOM 트리(BomNode 구조)를 생성해 반환
    public List<BomNode> makeBomNodes(Product product) {

        // 주어진 제품을 부모로 갖는 BOM 리스트 조회
        List<Bom> bomList = bomRepository.findByParentProduct(product);

        // BOM 트리를 구성할 노드 리스트
        List<BomNode> bomNodeList = new ArrayList<>();

        // 각 BOM 정보를 재귀적으로 BomNode 트리 구조로 변환
        for (Bom bom : bomList) {
            // 재귀로 하위 노드 포함해서 변환
            bomNodeList.add(convertToBomNode(bom));
        }

        // 최종 BOM 트리 반환
        return bomNodeList;
    }

    // 최종적으로 필요한 자재 목록(Demand)을 계산하는 메서드
    public List<Demand> calculateRequiredMaterials(Product product) {

        // 자재별 수량을 누적할 Map
        Map<Product, Integer> counter = new HashMap<>();

        // 제품 1개에 대해 필요한 자재를 재귀적으로 수집
        collectMaterial(product, 1, counter);

        // 누적된 자재 수량 정보를 Demand 객체로 변환하여 리스트로 반환
        List<Demand> demandList = counter.entrySet().stream().map((e) ->
                        Demand.builder()
                                .product(e.getKey())     // 자재(Product)
                                .quantity(e.getValue())  // 누적 수량
                                .build())
                .toList();

        /*

        또는
        List<Demand> demandList = new ArrayList<>();
        for(Product p  : counter.keySet()) {
            Demand d =  Demand.builder().product(p).quantity(counter.get(p)).build();
            demandList.add(d);
        }
        */

        return demandList;
    }

    // 자재 수량을 재귀적으로 누적하는 내부 메서드
    private void collectMaterial(Product product, int quantity, Map<Product, Integer> counter) {

        // 현재 제품을 부모로 가지는 BOM 리스트 조회
        List<Bom> bomList = bomRepository.findByParentProduct(product);

        // 자식이 없는 경우 → leaf node → 실제 자재 → 수량 누적
        if (bomList == null || bomList.isEmpty()) {
            int currVal = counter.getOrDefault(product, 0); // 현재까지 누적된 수량
            counter.put(product, currVal + quantity);                 // 현재 수량을 더해서 저장

            // 자식이 있는 경우 → 자식으로 재귀 호출
        } else {
            for (Bom bom : bomList) {
                collectMaterial(
                        bom.getChildProduct(),                    // 자식 제품
                        quantity * bom.getQuantity(),             // 부모 수량 × 자식 수량
                        counter);                                 // 누적 map 전달
            }
        }
    }


    // BOM 객체를 재귀적으로 BomNode로 변환하는 내부 메서드
    // 화면에서 트리 형태로 구성 표시할 때 사용
    private BomNode convertToBomNode(Bom bom) {

        // 현재 BOM의 자식 제품을 다시 부모로 갖는 하위 BOM 리스트 조회
        List<Bom> childBom = bomRepository.findByParentProduct(bom.getChildProduct());

        // 자식 노드들을 재귀적으로 BomNode로 변환
        List<BomNode> childBomNodes = new ArrayList<>();
        for (Bom bomChild : childBom) {
            childBomNodes.add(convertToBomNode(bomChild));
        }

        // 현재 노드를 BomNode로 생성하여 반환
        BomNode node = BomNode.builder().
                id(bom.getId().toString())
                .label(bom.getChildProduct().getName() + " X " + bom.getQuantity()) // 화면에 표시되는 문자열
                .children(childBomNodes)
                .build();
        return node;
    }

}
