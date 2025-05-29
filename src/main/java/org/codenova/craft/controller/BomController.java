package org.codenova.craft.controller;

import lombok.RequiredArgsConstructor;
import org.codenova.craft.entity.Bom;
import org.codenova.craft.repository.BomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class BomController {

    // BOM 정보를 조회하기 위한 리포지토리
    private final BomRepository bomRepository;

    // 전체 BOM 목록을 조회하는 API
    @GetMapping("/api/bom")
    public ResponseEntity<?> getAllBoms() {

        // DB에서 모든 BOM 데이터를 조회
        List<Bom> boms = bomRepository.findAll();

        // 응답 데이터를 담을 Map 생성
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);         // HTTP 응답 상태 코드
        response.put("boms", boms);          // 조회된 BOM 데이터 리스트
        response.put("total", boms.size());  // 전체 개수도 함께 전달

        return ResponseEntity.status(200).body(response);
    }
}