package com.example.final_project.service;

import com.example.final_project.dto.ProductDto;
import com.example.final_project.exceptions.ProductNotFoundException;
import com.example.final_project.mapper.ProductMapper;
import com.example.final_project.model.Product;
import com.example.final_project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(product -> ProductMapper.toDto(product)).collect(Collectors.toList());
    }

    public ProductDto getProductById(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException());
        return ProductMapper.toDto(product);
    }


    public ProductDto addProduct(ProductDto productDto) {
        Product product = productRepository.save(ProductMapper.fromDto(productDto));
        return ProductMapper.toDto(product);
    }


    public ProductDto updateProduct(int id, ProductDto productDto) {
        return ProductMapper.toDto(productRepository.findById(id)
                .map(product -> {
                    product.setName(productDto.getName());
                    product.setPrice(productDto.getPrice());
                    product.setProductCategory(productDto.getProductCategory());
                    product.setQuantity(productDto.getQuantity());
                    return productRepository.save(product);
                }).orElseThrow(() -> new ProductNotFoundException()));
    }

    public void deleteProductById(int id) {
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException());
        productRepository.deleteById(id);
    }
}
