package com.example.final_project.dto;

import com.example.final_project.model.ProductCategory;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class ProductDto {
    private Integer id;
    private String name;
    private double price;
    private int quantity;
    private ProductCategory productCategory;

}
