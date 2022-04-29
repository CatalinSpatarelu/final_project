package com.example.final_project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class ClientDto {

    private Integer id;

    private String name;

    private String email;

    private String address;

    private String role;

    private String password;

    private ShopCartDto shopCartDto;

    private FavoriteItemDto favoriteItemDto;

}
