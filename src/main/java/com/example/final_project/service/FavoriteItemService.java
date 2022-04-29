package com.example.final_project.service;

import com.example.final_project.dto.FavoriteItemDto;
import com.example.final_project.exceptions.FavoriteItemNotFoundException;
import com.example.final_project.mapper.FavoriteItemMapper;
import com.example.final_project.mapper.ProductMapper;
import com.example.final_project.model.FavoriteItem;
import com.example.final_project.repository.FavoriteItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteItemService {

    private FavoriteItemRepository favoriteItemRepository;

    @Autowired
    public FavoriteItemService(FavoriteItemRepository favoriteItemRepository) {
        this.favoriteItemRepository = favoriteItemRepository;
    }

    public List<FavoriteItemDto> getAllFavoriteList() {
        return favoriteItemRepository.findAll()
                .stream()
                .map(favoriteItem -> FavoriteItemMapper.toDto(favoriteItem)).collect(Collectors.toList());
    }

    public FavoriteItemDto getFavoriteItemById(int id) {
        FavoriteItem favoriteItem = favoriteItemRepository.findById(id)
                .orElseThrow(() -> new FavoriteItemNotFoundException());
        return FavoriteItemMapper.toDto(favoriteItem);
    }

    public FavoriteItemDto addFavoriteItem(FavoriteItemDto favoriteItemDto) {
        return FavoriteItemMapper.toDto(favoriteItemRepository.save(FavoriteItemMapper.fromDto(favoriteItemDto)));
    }

    public FavoriteItemDto updateFavoriteItem(int id, FavoriteItemDto favoriteItemDto) {
        return FavoriteItemMapper.toDto(favoriteItemRepository.findById(id)
                .map(favoriteItem -> {
                    if (favoriteItemDto.getProducts() != null) {
                        favoriteItem.setProducts(favoriteItemDto.getProducts()
                                .stream()
                                .map(productDto -> ProductMapper.fromDto(productDto))
                                .collect(Collectors.toSet()));
                    }
                    return favoriteItemRepository.save(favoriteItem);
                }).orElseThrow(() -> new FavoriteItemNotFoundException()));
    }

    public void deleteFavoriteItemById(int id) {
        favoriteItemRepository.findById(id)
                .orElseThrow(() -> new FavoriteItemNotFoundException());
        favoriteItemRepository.deleteById(id);
    }
}
