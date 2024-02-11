package com.example.store_management_tool.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDto {
    private UUID productCatalogNumber;
    private Double price;
    private String name;
    private String characteristics;
    private String picture;
}
