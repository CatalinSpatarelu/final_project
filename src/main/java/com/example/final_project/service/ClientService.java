package com.example.final_project.service;

import com.example.final_project.dto.ClientDto;
import com.example.final_project.exceptions.ClientNotFoundException;
import com.example.final_project.mapper.ClientMapper;
import com.example.final_project.mapper.FavoriteItemMapper;
import com.example.final_project.mapper.ShopCartMapper;
import com.example.final_project.model.Client;
import com.example.final_project.model.FavoriteItem;
import com.example.final_project.model.ShopCart;
import com.example.final_project.repository.ClientRepository;
import com.example.final_project.repository.FavoriteItemRepository;
import com.example.final_project.repository.ShopCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {


    private ClientRepository clientRepository;
    private ShopCartRepository shopCartRepository;
    private FavoriteItemRepository favoriteItemRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, ShopCartRepository shopCartRepository, FavoriteItemRepository favoriteItemRepository) {
        this.clientRepository = clientRepository;
        this.shopCartRepository = shopCartRepository;
        this.favoriteItemRepository = favoriteItemRepository;
    }

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(client -> ClientMapper.toDto(client))
                .collect(Collectors.toList());
    }

    public ClientDto getClientById(int id) {
        return ClientMapper.toDto(clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException()));
    }


    public ClientDto addClient(ClientDto clientDto, String password) {
        ShopCart shopCart = new ShopCart();
        FavoriteItem favoriteItem = new FavoriteItem();
        clientDto.setShopCartDto(ShopCartMapper.toDto(shopCart));
        clientDto.setFavoriteItemDto(FavoriteItemMapper.toDto(favoriteItem));
        Client client = ClientMapper.fromDto(clientDto);
        client.setPassword(password);
        client.setRole("user");
        return ClientMapper.toDto(clientRepository.save(client));
    }

    public ClientDto updateClient(int id, ClientDto clientDto) {
        return ClientMapper.toDto(clientRepository.findById(id)
                .map(client -> {
                    client.setEmail(clientDto.getEmail());
                    client.setName(clientDto.getName());
                    client.setAddress(clientDto.getAddress());
                    return clientRepository.save(client);
                }).orElseThrow(() -> new ClientNotFoundException()));
    }


    public void deleteClientById(int id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException());
        shopCartRepository.deleteById(client.getShopCart().getId());
        favoriteItemRepository.deleteById(client.getFavoriteItem().getId());
        clientRepository.deleteById(id);
    }

    public ClientDto getClientByEmailAndPassword(String email, String password) {
        return ClientMapper.toDto(clientRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ClientNotFoundException()));
    }
}
