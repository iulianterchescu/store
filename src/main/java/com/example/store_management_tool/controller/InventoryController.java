package com.example.store_management_tool.controller;

import com.example.store_management_tool.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final ProductService productService;

    @DeleteMapping("/delete/{productCatalogNumber}")
    public ResponseEntity<Void> removeProductFromInventory(@PathVariable UUID productCatalogNumber){
       productService.removeProductFromInventory(productCatalogNumber);
       return ResponseEntity.ok().build();
    }

    @GetMapping()
    private ResponseEntity<Map<UUID, Integer>> getProductsInventory(){
        return ResponseEntity.ok(productService.getProductsInventory());
    }

}
