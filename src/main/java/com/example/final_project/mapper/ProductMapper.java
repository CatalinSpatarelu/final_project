package com.example.final_project.mapper;

import com.example.final_project.dto.ProductDto;
import com.example.final_project.model.Product;

public class ProductMapper {

    public static Product fromDto(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setProductCategory(productDto.getProductCategory());
        return product;
    }

    public static ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        productDto.setProductCategory(product.getProductCategory());
        return productDto;
    }
}
