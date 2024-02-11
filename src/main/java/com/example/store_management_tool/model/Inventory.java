//package com.example.store_management_tool.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Data
//@Entity
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"product_catalor_number"})})
//public class Inventory {
//    @Id
//    @Column(name = "id", updatable = false, nullable = false)
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
//    private Product product;
//
//    private Integer numberOfPieces;
//
//}
