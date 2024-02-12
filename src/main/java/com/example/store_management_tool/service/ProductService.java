package com.example.store_management_tool.service;

import com.example.store_management_tool.exception.ProductNotAvailableException;
import com.example.store_management_tool.mapper.ProductMapper;
import com.example.store_management_tool.model.Product;
import com.example.store_management_tool.model.dtos.ProductDto;
import com.example.store_management_tool.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public ProductDto findProduct(UUID productCatalogNumber) {
        return mapper.toDto(getProduct(productCatalogNumber));
    }

    public void addProduct(ProductDto product, Integer numberOfProducts) {
        try {
            productRepository.save(mapper.fromDto(product, numberOfProducts));
            log.info("Product with product catalog number " + product.getProductCatalogNumber() + "was added to stock in " + numberOfProducts + " pieces");
        }catch (Exception e){
            throw e;
        }
    }

    public void changePrice(UUID productCatalogNumber, Double newPrice) {
        Product product = getProduct(productCatalogNumber);
        product.setPrice(newPrice);
        productRepository.save(product);
        log.info("The new price of product with UUID: " + productCatalogNumber + " is" + newPrice);
    }

    public ProductDto sellOneProduct(UUID productCatalogNumber) {
        Product product = getProduct(productCatalogNumber);
        checkProductAvailability(product, 1);
        reduceNumberOfProducts(product, 1);
        return mapper.toDto(product);
    }

    public Map<ProductDto, Integer> sellMoreProducts(Map<UUID, Integer> shoppingList) {
        List <Product> products = productRepository.findByProductCatalogNumberIn(shoppingList.keySet().stream().toList());
        Map<ProductDto, Integer> productsMap = new HashMap<>();
        products.forEach(product -> {
            checkProductAvailability(product, shoppingList.get(product.getProductCatalogNumber()));
            reduceNumberOfProducts(product,  shoppingList.get(product.getProductCatalogNumber()));
            productsMap.put(mapper.toDto(product), shoppingList.get(product.getProductCatalogNumber()));
        });
        return productsMap;
    }

    public void removeProductFromInventory(UUID productCatalogNumber) {
        productRepository.deleteByProductCatalogNumber(productCatalogNumber);
    }

    private Product getProduct(UUID productCatalogNumber){
        return productRepository.findByProductCatalogNumber(productCatalogNumber)
                .orElseThrow(() ->new ProductNotAvailableException("Product with catalog number: " + productCatalogNumber + " was not found"));
    }

    private Boolean checkProductAvailability(Product product, Integer numberOfProductsToCheck) {
        if (product.getNumberOfProducts() >= numberOfProductsToCheck) {
            return true;
        }
        throw new ProductNotAvailableException("Product stock is not enough");
    }

    private void reduceNumberOfProducts(Product product, Integer numberToReduce){
        product.setNumberOfProducts(product.getNumberOfProducts()-numberToReduce);
        productRepository.save(product);
    }
}
