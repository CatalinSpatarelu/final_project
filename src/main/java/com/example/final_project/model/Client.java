package com.example.final_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;


@Entity
@Table(name = "clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotBlank(message = "Name must not be blank")
    @NotNull(message = "Name must not be empty")
    private String name;
    @Email(message = "Invalid email")
    @NotNull(message = "Email must not be empty")
    private String email;
    @NotBlank(message = "The address must not be blank")
    @NotNull(message = "The address must not be empty")
    private String address;
    private String password;
    private String role;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "shop_cart_id")
    private ShopCart shopCart;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "favorite_items_id")
    private FavoriteItem favoriteItem;
}
