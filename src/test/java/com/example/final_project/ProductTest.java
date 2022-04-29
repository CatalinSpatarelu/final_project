package com.example.final_project;

import com.example.final_project.model.Product;
import com.example.final_project.model.ProductCategory;
import com.example.final_project.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class ProductTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void cleanDatabase() {
        productRepository.deleteAll();
    }

    @AfterEach
    public void cleanDatabaseAfter() {
        productRepository.deleteAll();
    }

    @Test
    public void test() {
        Product product = new Product();
        product.setName("produs");
        product.setProductCategory(ProductCategory.CLOTHES);
        product.setPrice(15.67);
        product.setQuantity(1);
        Product savedProduct = productRepository.save(product);
        Assertions.assertNotNull(savedProduct);
        Assertions.assertEquals("produs", savedProduct.getName());
        Assertions.assertEquals(ProductCategory.CLOTHES,savedProduct.getProductCategory());
        Assertions.assertEquals(15.67,savedProduct.getPrice());
        Assertions.assertEquals(1,savedProduct.getQuantity());
    }
}
