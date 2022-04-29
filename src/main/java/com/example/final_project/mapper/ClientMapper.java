package com.example.final_project.mapper;

import com.example.final_project.dto.ClientDto;
import com.example.final_project.model.Client;

public class ClientMapper {

    public static Client fromDto(ClientDto clientDto) {
        Client client = new Client();
        client.setId(clientDto.getId());
        client.setName(clientDto.getName());
        client.setEmail(clientDto.getEmail());
        client.setAddress(clientDto.getAddress());
        client.setShopCart(ShopCartMapper.fromDto(clientDto.getShopCartDto()));
        client.setFavoriteItem(FavoriteItemMapper.fromDto(clientDto.getFavoriteItemDto()));
        client.setRole(clientDto.getRole());
        return client;
    }

    public static ClientDto toDto(Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        clientDto.setEmail(client.getEmail());
        clientDto.setAddress(client.getAddress());
        clientDto.setShopCartDto(ShopCartMapper.toDto(client.getShopCart()));
        clientDto.setFavoriteItemDto(FavoriteItemMapper.toDto(client.getFavoriteItem()));
        clientDto.setRole(client.getRole());
        return clientDto;
    }
}
