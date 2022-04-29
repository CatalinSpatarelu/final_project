package com.example.final_project;

import com.example.final_project.dto.ProductDto;
import com.example.final_project.exceptions.ProductNotFoundException;
import com.example.final_project.mapper.ProductMapper;
import com.example.final_project.model.Product;
import com.example.final_project.model.ProductCategory;
import com.example.final_project.repository.ProductRepository;
import com.example.final_project.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProductsTest() {
        Product productOne = new Product();
        Product productTwo = new Product();
        List<Product> expectedList = List.of(productOne, productTwo);
        Mockito.when(productRepository.findAll()).thenReturn(expectedList);
        List<ProductDto> actualList = productService.getAllProducts();
        List<Product> actualListProduct = actualList
                .stream()
                .map(productDto -> ProductMapper.fromDto(productDto))
                .collect(Collectors.toList());
        Assertions.assertNotNull(actualList);
        Assertions.assertEquals(2, actualList.size());
        Assertions.assertEquals(expectedList, actualListProduct);

    }

    @Test
    void getProductByIdTest() {
        Product product = new Product(1, "product", 15.5, 5, ProductCategory.CLOTHES);

        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        ProductDto productExpected = productService.getProductById(product.getId());

        Assertions.assertNotNull(productExpected);
        Assertions.assertEquals(productExpected, ProductMapper.toDto(product));
    }

    @Test
    void getProductByIdThrowExcpetion() {

        Mockito.when(productRepository.findById(ArgumentMatchers.anyInt())).thenThrow(ProductNotFoundException.class);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductById(ArgumentMatchers.anyInt()));
    }

    @Test
    void addProductTest() {
        Product product = new Product(1, "product", 15.5, 5, ProductCategory.CLOTHES);

        Mockito.when(productRepository.save(product)).thenReturn(product);

        ProductDto productAdded = productService.addProduct(ProductMapper.toDto(product));

        Mockito.verify(productRepository).save(product);

        Assertions.assertNotNull(productAdded);
        Assertions.assertEquals(ProductMapper.fromDto(productAdded), product);
        Assertions.assertEquals(1, productAdded.getId());
        Assertions.assertEquals("product", productAdded.getName());
        Assertions.assertEquals(15.5, productAdded.getPrice());
        Assertions.assertEquals(5, productAdded.getQuantity());
        Assertions.assertEquals(ProductCategory.CLOTHES, productAdded.getProductCategory());
    }

    @Test
    void updateProductTest() {
        Product product = new Product(1, "product", 15.5, 5, ProductCategory.CLOTHES);

        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        ProductDto prodductExpected = productService.updateProduct(1, ProductMapper.toDto(product));

        Mockito.verify(productRepository).findById(product.getId());

        Mockito.verify(productRepository).save(product);

        Assertions.assertNotNull(prodductExpected);
        Assertions.assertEquals(ProductMapper.fromDto(prodductExpected), product);
        Assertions.assertEquals(1, prodductExpected.getId());
        Assertions.assertEquals("product", prodductExpected.getName());
        Assertions.assertEquals(15.5, prodductExpected.getPrice());
        Assertions.assertEquals(5, prodductExpected.getQuantity());
        Assertions.assertEquals(ProductCategory.CLOTHES, prodductExpected.getProductCategory());
    }

    @Test
    void updateProductThrowExceptionTest() {
        Product product = new Product();
        product.setId(1);
        Mockito.when(productRepository.findById(ArgumentMatchers.anyInt())).thenThrow(ProductNotFoundException.class);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(product.getId(), ProductMapper.toDto(product)));
    }

    @Test
    void deleteProductByIdTest() {
        Product product = new Product();
        product.setId(1);
        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        productService.deleteProductById(product.getId());

        Mockito.verify(productRepository).deleteById(product.getId());
    }

    @Test
    void deleteProductByIdThrowExceptionTest() {

        Mockito.when(productRepository.findById(ArgumentMatchers.anyInt())).thenThrow(ProductNotFoundException.class);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.deleteProductById(ArgumentMatchers.anyInt()));
    }

}
