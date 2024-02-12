package com.example.store_management_tool.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "product", uniqueConstraints = {@UniqueConstraint(columnNames = {"product_catalor_number"})})
public class Product {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_catalor_number")
    private UUID productCatalogNumber;
    @Column(name = "price")
    private Double price;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(name = "number_of_products")
    private Integer numberOfProducts;
    @Column(name = "name")
    private String name;
    @Column(name = "characteristics")
    private String characteristics;
    @Column(name = "picture")
    private String picture;
}
