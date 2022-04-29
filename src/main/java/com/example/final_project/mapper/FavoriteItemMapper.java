package com.example.final_project.mapper;

import com.example.final_project.dto.FavoriteItemDto;
import com.example.final_project.model.FavoriteItem;

import java.util.Collections;
import java.util.stream.Collectors;

public class FavoriteItemMapper {

    public static FavoriteItem fromDto(FavoriteItemDto favoriteItemDto) {
        FavoriteItem favoriteItem = new FavoriteItem();
        favoriteItem.setId(favoriteItemDto.getId());
        if (favoriteItemDto.getProducts() != null) {
            favoriteItem.setProducts(favoriteItemDto.getProducts()
                    .stream().map(productDto -> ProductMapper.fromDto(productDto)).collect(Collectors.toSet()));
        } else {
            favoriteItem.setProducts(Collections.emptySet());
        }

        return favoriteItem;
    }

    public static FavoriteItemDto toDto(FavoriteItem favoriteItem) {
        FavoriteItemDto favoriteItemDto = new FavoriteItemDto();
        favoriteItemDto.setId(favoriteItem.getId());
        if (favoriteItem.getProducts() != null) {
            favoriteItemDto.setProducts(favoriteItem.getProducts()
                    .stream().map(product -> ProductMapper.toDto(product)).collect(Collectors.toSet()));
        } else {
            favoriteItemDto.setProducts(Collections.emptySet());
        }
        return favoriteItemDto;
    }
}
