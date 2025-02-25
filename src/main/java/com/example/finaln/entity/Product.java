package com.example.finaln.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String imageUrl;
    private String name;
    private int price;
    private int sales;
    private int stock;
    private String color;
    private String size;


}