package com.example.final_project.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class ShopCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private double discount;
    @ManyToMany
    @JoinTable(
            name = "shop_cart_products",
            joinColumns = @JoinColumn(name = "shop_cart_id"),
            inverseJoinColumns = @JoinColumn(name = "products_id")
    )
    private Set<Product> products;
}
