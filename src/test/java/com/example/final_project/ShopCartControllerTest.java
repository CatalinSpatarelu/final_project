package com.example.final_project;


import com.example.final_project.dto.ShopCartDto;
import com.example.final_project.mapper.ShopCartMapper;
import com.example.final_project.model.ShopCart;
import com.example.final_project.repository.ShopCartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class ShopCartControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ShopCartRepository shopCartRepository;


    @BeforeEach
    public void cleanDatabase() {
        shopCartRepository.deleteAll();
    }

    @Test
    void getAllShopCartsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/shopCarts")
                        .header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    void getShopCartById() throws Exception {
        ShopCart shopCart = new ShopCart();
        shopCart.setProducts(Collections.emptySet());
        ShopCart savedShopCart = shopCartRepository.save(shopCart);

        mockMvc.perform(MockMvcRequestBuilders.get("/shopCarts/" + savedShopCart.getId())
                        .header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(savedShopCart)));
    }

    @Test
    void getShopCartByIdThrowExceptionTest() throws Exception {
        mockMvc.perform((MockMvcRequestBuilders.get("/shopCarts/" + ArgumentMatchers.anyInt())
                        .header("Authorization", "Basic dGVzdDp0ZXN0")))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Shop cart not found"));
    }

    @Test
    void updateShopCartTest() throws Exception {
        ShopCartDto shopCartDto = new ShopCartDto();
        ShopCart savedShopCart = shopCartRepository.save(ShopCartMapper.fromDto(shopCartDto));

        ShopCartDto shopCartToUpdate = new ShopCartDto();
        shopCartToUpdate.setDiscount(10);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/shopCarts/" + savedShopCart.getId())
                        .header("Authorization", "Basic dGVzdDp0ZXN0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shopCartToUpdate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        ShopCartDto shopCartReturned = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ShopCartDto.class);

        Assertions.assertNotNull(shopCartReturned);
        Assertions.assertEquals(savedShopCart.getId(), shopCartReturned.getId());
        Assertions.assertEquals(shopCartToUpdate.getDiscount(), shopCartReturned.getDiscount());
    }

    @Test
    void updateShopCartThrowExceptionTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/shopCarts/" + ArgumentMatchers.anyInt())
                        .header("Authorization", "Basic dGVzdDp0ZXN0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShopCartDto())))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Shop cart not found"));
    }

    @Test
    void deleteShopCartByIdTest() throws Exception {
        ShopCartDto shopCart = new ShopCartDto();
        ShopCart savedShopCart = shopCartRepository.save(ShopCartMapper.fromDto(shopCart));

        mockMvc.perform(MockMvcRequestBuilders.delete("/shopCarts/" + savedShopCart.getId())
                        .header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(""));

        Assertions.assertEquals(Optional.empty(), shopCartRepository.findById(savedShopCart.getId()));
    }

    @Test
    void deleteShopCartByIdThrowExceptionTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/shopCarts/" + ArgumentMatchers.anyInt())
                        .header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Shop cart not found"));
    }
}
