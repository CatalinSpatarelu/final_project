package com.example.final_project.mapper;

import com.example.final_project.dto.ShopCartDto;
import com.example.final_project.model.ShopCart;

import java.util.Collections;
import java.util.stream.Collectors;

public class ShopCartMapper {

    public static ShopCart fromDto(ShopCartDto shopCartDto) {
        ShopCart shopCart = new ShopCart();
        shopCart.setId(shopCartDto.getId());
        shopCart.setDiscount(shopCartDto.getDiscount());
        if (shopCartDto.getProducts() != null) {
            shopCart.setProducts(shopCartDto.getProducts()
                    .stream().map(productDto -> ProductMapper.fromDto(productDto)).collect(Collectors.toSet()));
        } else {

            shopCart.setProducts(Collections.emptySet());
        }

        return shopCart;
    }

    public static ShopCartDto toDto(ShopCart shopCart) {
        ShopCartDto shopCartDto = new ShopCartDto();
        shopCartDto.setId(shopCart.getId());
        shopCartDto.setDiscount(shopCart.getDiscount());
        if (shopCart.getProducts() != null) {
            shopCartDto.setProducts(shopCart.getProducts()
                    .stream().map(product -> ProductMapper.toDto(product)).collect(Collectors.toSet()));
        } else {
            shopCartDto.setProducts(Collections.emptySet());
        }

        return shopCartDto;
    }
}
