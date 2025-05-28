package org.codenova.craft.controller;


import lombok.RequiredArgsConstructor;
import org.codenova.craft.entity.Product;
import org.codenova.craft.repository.BomRepository;
import org.codenova.craft.repository.ProductRepository;
import org.codenova.craft.response.BomNode;
import org.codenova.craft.response.Demand;
import org.codenova.craft.service.BomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final BomRepository bomRepository;
    private final BomService bomService;

    // 전체 제품 목록을 조회하는 API
    @GetMapping("/api/product")
    public ResponseEntity<?> getAllProducts() {

        // 모든 제품 데이터를 DB에서 조회
        List<Product> products = productRepository.findAll();

        // 응답 데이터를 담을 Map 생성
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);             // 상태 코드
        response.put("products", products);      // 제품 목록
        response.put("total", products.size());  // 총 개수

        return ResponseEntity.status(200).body(response);
    }

    // 특정 제품의 상세 정보 및 BOM 트리 조회 API
    @GetMapping("/api/product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {

        // 제품 ID로 제품 조회 (없으면 예외 발생)
        Product product = productRepository.findById(productId).orElseThrow();

        // 응답 데이터를 담을 Map 생성
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);        // 상태 코드
        response.put("product", product);   // 제품 정보

        // 해당 제품 기준으로 BOM 트리 구조 생성
        List<BomNode> bomNodeList = bomService.makeBomNodes(product);

        // BOM 트리 노드 리스트 응답에 포함
        response.put("boms", bomNodeList);

        List<Demand> demand = bomService.calculateRequiredMaterials(product);
        response.put("materials", demand);

        return ResponseEntity.status(200).body(response);
    }
}
