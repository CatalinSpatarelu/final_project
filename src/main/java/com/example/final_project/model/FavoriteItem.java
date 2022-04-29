package com.example.final_project.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "favorite_items")
@Data
public class FavoriteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToMany
    @JoinTable(
            name = "favorite_items_products",
            joinColumns = @JoinColumn(name = "favorite_items_id"),
            inverseJoinColumns = @JoinColumn(name = "products_id")
    )
    private Set<Product> products;
}
