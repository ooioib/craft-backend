package org.codenova.craft.controller;

import lombok.RequiredArgsConstructor;
import org.codenova.craft.entity.Part;
import org.codenova.craft.repository.PartRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class PartController {
    private final PartRepository partRepository;

    // 전체 자재 목록을 조회하는 API
    @GetMapping("/api/part")
    public ResponseEntity<?> allParts() {

        List<Part> parts = partRepository.findAll();

        // 응답 데이터를 담을 Map 생성
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);          // 상태 코드
        response.put("parts", parts);         // 자재 목록
        response.put("total", parts.size());  // 총 개수

        return ResponseEntity.status(200).body(null);

    }
}
