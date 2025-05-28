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

    public List<BomNode> makeBomNodes(Product product) {
        List<Bom> bomList = bomRepository.findByParentProduct(product);

        List<BomNode> bomNodeList = new ArrayList<>();
        for (Bom bom : bomList) {
            bomNodeList.add(convertToBomNode(bom));
        }

        return bomNodeList;
    }

    public List<Demand> calculateRequiredMaterials(Product product) {
        Map<Product, Integer> counter = new HashMap<>();
        collectMaterial(product, 1, counter);

        List<Demand> demandList = counter.entrySet().stream().map((e) ->
                        Demand.builder()
                                .product(e.getKey())
                                .quantity(e.getValue())
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

    private void collectMaterial(Product product, int quantity, Map<Product, Integer> counter) {

        List<Bom> bomList = bomRepository.findByParentProduct(product);
        if (bomList == null || bomList.isEmpty()) {
            Integer currVal = counter.getOrDefault(product, 0);
            counter.put(product, currVal + quantity);

        } else {
            for (Bom bom : bomList) {
                collectMaterial(
                        bom.getChildProduct(),
                        quantity * bom.getQuantity(),
                        counter);
            }
        }
    }


    private BomNode convertToBomNode(Bom bom) {

        List<Bom> childBom = bomRepository.findByParentProduct(bom.getChildProduct());

        List<BomNode> childBomNodes = new ArrayList<>();
        for (Bom bomChild : childBom) {
            childBomNodes.add(convertToBomNode(bomChild));
        }

        BomNode node = BomNode.builder().
                id(bom.getId().toString())
                .label(bom.getChildProduct().getName() + " X " + bom.getQuantity())
                .children(childBomNodes)
                .build();
        return node;
    }

}
