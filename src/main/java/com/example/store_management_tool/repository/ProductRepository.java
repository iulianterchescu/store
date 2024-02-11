package com.example.store_management_tool.repository;

import com.example.store_management_tool.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductCatalogNumber(UUID productCatalogNumber);

    List<Product> findByProductCatalogNumberIn(List<UUID> productCatalogNumbers);

    void deleteByProductCatalogNumber(UUID productCatalogNumber);
}
