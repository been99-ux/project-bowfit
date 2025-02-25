package com.example.finaln.dto;


import com.example.finaln.entity.Product;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProductDto {
    private Long id;
    private String category;
    private String imageUrl;
    private String name;
    private int price;
    private int sales;
    private int stock;
    private String color;
    private String size;


    public AdminProductDto(Product product) {
        this.id = product.getId();
        this.category = product.getCategory();
        this.imageUrl = product.getImageUrl();
        this.name = product.getName();
        this.price = product.getPrice();
        this.sales = product.getSales();
        this.stock = product.getStock();
        this.color = product.getColor();
        this.size = product.getSize();

    }



}