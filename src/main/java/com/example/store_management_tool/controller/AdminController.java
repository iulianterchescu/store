package com.example.store_management_tool.controller;

import com.example.store_management_tool.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin")
//@HttpConstraint(rolesAllowed = "ADMIN")
public class AdminController {
    private final ProductService productService;

    @DeleteMapping
    public void removeProductFromInventory(UUID productCatalogNumber){
        productService.removeProductFromInventory(productCatalogNumber);
    }

    @GetMapping()
    private void getProductsInventory(){
        //returneaza o mapa in care cheia este uuid-ul produsului si valoarea este numarul de bucati din produsul respectiv
    }
}
