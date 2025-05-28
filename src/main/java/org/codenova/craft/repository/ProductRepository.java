package org.codenova.craft.repository;

import org.codenova.craft.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    public List<Product> findAllByType(String type);

}

