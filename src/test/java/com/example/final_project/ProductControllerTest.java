package com.example.final_project;


import com.example.final_project.dto.ProductDto;
import com.example.final_project.repository.ProductRepository;
import com.example.final_project.service.ProductService;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void cleanDatabase() {
        productRepository.deleteAll();
    }

    @Test
    void getAllProductsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .header("Authorization","Basic dGVzdDp0ZXN0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    void getProductByIdTest() throws Exception {
        ProductDto product = new ProductDto();
        ProductDto productAdded = productService.addProduct(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/"+productAdded.getId())
                        .header("Authorization","Basic dGVzdDp0ZXN0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(productAdded)));

    }

    @Test
    void getProductByIdThrowExceptionTest() throws Exception {
        mockMvc.perform((MockMvcRequestBuilders.get("/products/" + ArgumentMatchers.anyInt())
                        .header("Authorization","Basic dGVzdDp0ZXN0")))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Product not found"));
    }

    @Test
    void updateProductTest() throws Exception {
        ProductDto product = new ProductDto();
        product.setName("product");
        ProductDto productDto = productService.addProduct(product);

        ProductDto productToUpdate = new ProductDto();
        productToUpdate.setName("product updated");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/products/" + productDto.getId())
                        .header("Authorization","Basic dGVzdDp0ZXN0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToUpdate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        ProductDto productReturned = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ProductDto.class);

        Assertions.assertNotNull(productReturned);
        Assertions.assertEquals(productDto.getId(), productReturned.getId());
        Assertions.assertEquals("product updated", productReturned.getName());
    }

    @Test
    void updateProductThrowExceptionTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/products/" + ArgumentMatchers.anyInt())
                        .header("Authorization","Basic dGVzdDp0ZXN0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductDto())))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Product not found"));
    }

    @Test
    void createProductTest() throws Exception {
        ProductDto product = new ProductDto();
        product.setName("product");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .header("Authorization","Basic dGVzdDp0ZXN0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        ProductDto productCreated = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ProductDto.class);

        Assertions.assertNotNull(productCreated);
        Assertions.assertNotNull(productCreated.getId());
        Assertions.assertEquals("product", productCreated.getName());
    }

    @Test
    void deleteProductByIdTest() throws Exception {
        ProductDto product = new ProductDto();
        product.setName("product");
        ProductDto productAdded = productService.addProduct(product);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + productAdded.getId())
                        .header("Authorization","Basic dGVzdDp0ZXN0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(""));

        Assertions.assertEquals(Optional.empty(), productRepository.findById(productAdded.getId()));
    }

    @Test
    void deleteProductByIdthrowExceptionTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + ArgumentMatchers.anyInt())
                        .header("Authorization","Basic dGVzdDp0ZXN0"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Product not found"));
    }
}
