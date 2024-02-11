package com.example.store_management_tool.controller;

import com.example.store_management_tool.model.ProductDto;
import com.example.store_management_tool.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
//@HttpConstraint(rolesAllowed = "USER")
public class ProductController {
    private final ProductService service;

    @GetMapping("/{productCatalogNumber}")
    public ResponseEntity<ProductDto> findProduct(@PathVariable("productCatalogNumber") UUID productCatalogNumber){
        return ResponseEntity.ok(service.findProduct(productCatalogNumber));
    }

    @PostMapping("/add/{productCatalogNumber}")
    public void addProduct(@RequestBody ProductDto product,
                           @PathVariable Integer numberOfProducts){
        service.addProduct(product, numberOfProducts);
    }

    @PutMapping("/changePrice/{productCatalogNumber}/{newPrice}")
    public void changePrice(@PathVariable UUID productCatalogNumber,
                            @PathVariable Double newPrice){
        service.changePrice(productCatalogNumber, newPrice);
    }

    @PutMapping("sellOne/{productCatalogNumber}")
    public ResponseEntity<ProductDto> sellOneProduct(@PathVariable UUID productCatalogNumber){
        return ResponseEntity.ok(service.sellOneProduct(productCatalogNumber));
    }

    @PutMapping("/sellMore")
    public ResponseEntity<Map<ProductDto, Integer>> sellMoreProducts(@RequestParam Map<UUID, Integer> shoppingList){
        return ResponseEntity.ok(service.sellMoreProducts(shoppingList));
    }

//    @GetMapping("/stock/{productCatalogNumber}")
//    public ResponseEntity<Integer> getNumberOfProductsAvailable(@PathVariable("productCatalogNumber") String productCatalogNumber){
//
//    }

}
