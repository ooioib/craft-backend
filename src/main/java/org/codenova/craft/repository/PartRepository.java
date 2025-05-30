package org.codenova.craft.repository;

import org.codenova.craft.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartRepository extends JpaRepository<Part, String> {
    public List<Part> findBySiteId(String siteId);

}
