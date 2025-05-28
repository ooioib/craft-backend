package org.codenova.craft.repository;

import org.codenova.craft.entity.Bom;
import org.codenova.craft.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BomRepository extends JpaRepository<Bom, Long> {
    public List<Bom> findByParentProduct(Product product);

}
