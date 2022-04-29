package com.example.final_project.service;

import com.example.final_project.dto.ShopCartDto;
import com.example.final_project.exceptions.ShopCartNotFoundException;
import com.example.final_project.mapper.ProductMapper;
import com.example.final_project.mapper.ShopCartMapper;
import com.example.final_project.model.ShopCart;
import com.example.final_project.repository.ShopCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShopCartService {

    private ShopCartRepository shopCartRepository;

    @Autowired
    public ShopCartService(ShopCartRepository shopCartRepository) {
        this.shopCartRepository = shopCartRepository;
    }

    public List<ShopCartDto> getAllShopCartList() {
        return shopCartRepository.findAll().stream().map(shopCart -> ShopCartMapper.toDto(shopCart)).collect(Collectors.toList());
    }

    public ShopCartDto getShopCartById(int id) {

        ShopCart shopCart = shopCartRepository.findById(id).orElseThrow(() -> new ShopCartNotFoundException());
        return ShopCartMapper.toDto(shopCart);
    }

    public ShopCartDto updateShopCart(int id, ShopCartDto shopCartDto) {
        return ShopCartMapper.toDto(shopCartRepository.findById(id)
                .map(shopCart -> {
                    if (shopCartDto.getProducts() != null) {
                        shopCart.setProducts(shopCartDto.getProducts()
                                .stream()
                                .map(productDto -> ProductMapper.fromDto(productDto))
                                .collect(Collectors.toSet()));
                    }
                    shopCart.setDiscount(shopCartDto.getDiscount());
                    return shopCartRepository.save(shopCart);
                }).orElseThrow(() -> new ShopCartNotFoundException()));
    }

    public void deleteShopCartById(int id) {
        shopCartRepository.findById(id).orElseThrow(() -> new ShopCartNotFoundException());

        shopCartRepository.deleteById(id);
    }
}
