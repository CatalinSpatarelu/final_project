package com.example.final_project;


import com.example.final_project.dto.ShopCartDto;
import com.example.final_project.exceptions.ShopCartNotFoundException;
import com.example.final_project.mapper.ShopCartMapper;
import com.example.final_project.model.ShopCart;
import com.example.final_project.repository.ShopCartRepository;
import com.example.final_project.service.ShopCartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class ShopCartServiceTest {

    @Mock
    private ShopCartRepository shopCartRepository;

    @InjectMocks
    private ShopCartService shopCartService;

    @Test
    void getAllShopCartListTest() {
        ShopCart shopCartOne = new ShopCart();
        ShopCart shopCartTwo = new ShopCart();
        shopCartOne.setProducts(Collections.emptySet());
        shopCartTwo.setProducts(Collections.emptySet());
        List<ShopCart> expectedList = List.of(shopCartOne, shopCartTwo);
        Mockito.when(shopCartRepository.findAll()).thenReturn(expectedList);
        List<ShopCartDto> actualList = shopCartService.getAllShopCartList();
        List<ShopCart> actualListShopCart = actualList
                .stream()
                .map(shopCartDto -> ShopCartMapper.fromDto(shopCartDto))
                .collect(Collectors.toList());
        Assertions.assertNotNull(actualList);
        Assertions.assertEquals(2, actualList.size());
        Assertions.assertEquals(expectedList, actualListShopCart);
    }

    @Test
    void getShopCartById() {
        ShopCart shopCart = new ShopCart();
        shopCart.setId(1);
        Mockito.when(shopCartRepository.findById(shopCart.getId())).thenReturn(Optional.of(shopCart));
        ShopCartDto shopCartExpected = shopCartService.getShopCartById(shopCart.getId());

        Assertions.assertNotNull(shopCartExpected);
        Assertions.assertEquals(shopCartExpected, ShopCartMapper.toDto(shopCart));
    }

    @Test
    void getShopCartByIdThrowExceptionTest() {

        Mockito.when(shopCartRepository.findById(ArgumentMatchers.anyInt())).thenThrow(ShopCartNotFoundException.class);

        Assertions.assertThrows(ShopCartNotFoundException.class, () -> shopCartService.getShopCartById(ArgumentMatchers.anyInt()));
    }


    @Test
    void updateShopCarThrowExceptionTest() {
        ShopCart shopCart = new ShopCart();
        shopCart.setId(1);
        Mockito.when(shopCartRepository.findById(shopCart.getId())).thenThrow(ShopCartNotFoundException.class);

        Assertions.assertThrows(ShopCartNotFoundException.class, () -> shopCartService.updateShopCart(shopCart.getId(), ShopCartMapper.toDto(shopCart)));
    }

    @Test
    void deleteShopCartByIdTest() {
        ShopCart shopCart = new ShopCart();
        shopCart.setId(1);

        Mockito.when(shopCartRepository.findById(shopCart.getId())).thenReturn(Optional.of(shopCart));

        shopCartService.deleteShopCartById(shopCart.getId());

        Mockito.verify(shopCartRepository).deleteById(shopCart.getId());
    }

    @Test
    void deleteShopCartByIdThrowsExceptionTest() {

        Mockito.when(shopCartRepository.findById(ArgumentMatchers.anyInt())).thenThrow(ShopCartNotFoundException.class);

        Assertions.assertThrows(ShopCartNotFoundException.class, () -> shopCartService.deleteShopCartById(ArgumentMatchers.anyInt()));
    }
}
