package com.example.store_management_tool.mapper;

import com.example.store_management_tool.model.Product;
import com.example.store_management_tool.model.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product){
        return product != null ? ProductDto.builder()
                .characteristics(product.getCharacteristics())
                .name(product.getName())
                .picture(product.getPicture())
                .price(product.getPrice())
                .productCatalogNumber(product.getProductCatalogNumber())
                .build() : ProductDto.builder().build();
    }

    public Product fromDto(ProductDto product, Integer numberOfProducts){
        return product != null ? Product.builder()
                .characteristics(product.getCharacteristics())
                .name(product.getName())
                .picture(product.getPicture())
                .price(product.getPrice())
                .productCatalogNumber(product.getProductCatalogNumber())
                .numberOfProducts(numberOfProducts)
                .build() : Product.builder().build();
    }
}
