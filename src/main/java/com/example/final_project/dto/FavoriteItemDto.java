package com.example.final_project.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class FavoriteItemDto implements Serializable {
    private Integer id;
    private Set<ProductDto> products;

}
