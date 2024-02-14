package com.example.store_management_tool.service;

import com.example.store_management_tool.exception.EmptyInventoryException;
import com.example.store_management_tool.exception.ProductNotAvailableException;
import com.example.store_management_tool.exception.SaveProductException;
import com.example.store_management_tool.mapper.ProductMapper;
import com.example.store_management_tool.model.Product;
import com.example.store_management_tool.model.dtos.ProductDto;
import com.example.store_management_tool.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
        saveProduct(mapper.fromDto(product, numberOfProducts));
        log.info("Product with product catalog number " + product.getProductCatalogNumber()
                + "was added to stock in " + numberOfProducts + " pieces");
    }

    public void changePrice(UUID productCatalogNumber, Double newPrice) {
        Product product = getProduct(productCatalogNumber);
        product.setPrice(newPrice);
        saveProduct(product);
        log.info("The new price of product with UUID: " + productCatalogNumber + " is " + newPrice);
    }

    public ProductDto sellOneProduct(UUID productCatalogNumber) {
        Product product = getProduct(productCatalogNumber);
        checkProductAvailabilityAndReduce(product, 1);
        return mapper.toDto(product);
    }

    public Map<ProductDto, Integer> sellMoreProducts(Map<UUID, Integer> shoppingList) {
        Map<ProductDto, Integer> productsMap = new HashMap<>();
        getProductInList(shoppingList).forEach(product -> {
            checkProductAvailabilityAndReduce(product, shoppingList.get(product.getProductCatalogNumber()));
            productsMap.put(mapper.toDto(product), shoppingList.get(product.getProductCatalogNumber()));
        });
        return productsMap;
    }

    @Transactional
    public void removeProductFromInventory(UUID productCatalogNumber) {
        productRepository.deleteByProductCatalogNumber(productCatalogNumber);
    }

    public Map<UUID, Integer> getProductsInventory() {
        List<Product> products = productRepository.findAll();
        if(ObjectUtils.isEmpty(products)){
            throw new EmptyInventoryException("Product inventory is empty");
        }
        Map<UUID, Integer> inventoryMap = new HashMap<>();
        products.forEach(product -> inventoryMap.put(product.getProductCatalogNumber(), product.getNumberOfProducts()));
        return inventoryMap;
    }

    private Product getProduct(UUID productCatalogNumber){
        log.info("Getting product with with catalog number:" + productCatalogNumber);
        return productRepository.findByProductCatalogNumber(productCatalogNumber)
                .orElseThrow(() -> new ProductNotAvailableException("Product with catalog number: " + productCatalogNumber + " was not found"));
    }

    private void checkProductAvailabilityAndReduce(Product product, Integer numberToReduce) {
        if (product.getNumberOfProducts() <= numberToReduce){
            log.info("Number of products is smaller than number of products to check");
            throw new ProductNotAvailableException("Product stock is not enough");
        }

        product.setNumberOfProducts(product.getNumberOfProducts()-numberToReduce);
        saveProduct(product);

    }

    private void saveProduct(Product product){
        try {
            productRepository.save(product);
            log.info("Product with product catalog number "+ product.getProductCatalogNumber()
                    + " was saved in database");
        }catch (Exception e){
            log.info("Product with product catalog number "+ product.getProductCatalogNumber()
                    + "was not saved in database");
            throw new SaveProductException("Product was not saved");
        }
    }

    private List<Product> getProductInList(Map<UUID, Integer> shoppingList){
        try {
            return productRepository.findByProductCatalogNumberIn(shoppingList.keySet().stream().toList());
        }catch (Exception e){
            throw new ProductNotAvailableException("Cannot retrieve product list from database");
        }
    }
}
