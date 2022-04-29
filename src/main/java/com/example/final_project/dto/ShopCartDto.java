package com.example.final_project.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data

public class ShopCartDto implements Serializable {
    private Integer id;
    private double discount;
    private Set<ProductDto> products;
}
