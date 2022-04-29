package com.example.final_project;

import com.example.final_project.dto.ClientDto;
import com.example.final_project.dto.FavoriteItemDto;
import com.example.final_project.dto.ShopCartDto;
import com.example.final_project.exceptions.ClientNotFoundException;
import com.example.final_project.mapper.ClientMapper;
import com.example.final_project.model.Client;
import com.example.final_project.model.FavoriteItem;
import com.example.final_project.model.ShopCart;
import com.example.final_project.repository.ClientRepository;
import com.example.final_project.repository.FavoriteItemRepository;
import com.example.final_project.repository.ShopCartRepository;
import com.example.final_project.service.ClientService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ShopCartRepository shopCartRepository;
    @Mock
    private FavoriteItemRepository favoriteItemRepository;
    @InjectMocks
    private ClientService clientService;


    @Test()
    void getAllClientsTest() {
        Client clientOne = new Client();
        clientOne.setName("catalin");
        clientOne.setAddress("address");
        clientOne.setEmail("catalin@gmail.com");
        clientOne.setShopCart(new ShopCart());
        clientOne.setFavoriteItem(new FavoriteItem());
        Client clientTwo = new Client();
        clientTwo.setName("ioan");
        clientTwo.setAddress("address");
        clientTwo.setEmail("ioan@gmail.com");
        clientTwo.setFavoriteItem(new FavoriteItem());
        clientTwo.setShopCart(new ShopCart());
        List<Client> expectedList = List.of(clientOne, clientTwo);
        Mockito.when(clientRepository.findAll()).thenReturn(expectedList);
        List<ClientDto> expectedListDto = expectedList.stream()
                .map(client -> ClientMapper.toDto(client)).collect(Collectors.toList());
        List<ClientDto> actualList = clientService.getAllClients();
        Assertions.assertNotNull(actualList);
        Assertions.assertEquals(2, actualList.size());
        Assertions.assertEquals(expectedListDto, actualList);
    }

    @Test
    void getClientById() {
        Client client = new Client(1, "catalin", "catalin@gmail", "bucuresti", "123", "", new ShopCart(), new FavoriteItem());
        Mockito.when(clientRepository.findById(1)).thenReturn(Optional.of(client));
        ClientDto clientDtoReturned = clientService.getClientById(1);
        Assertions.assertNotNull(clientDtoReturned);
        Assertions.assertEquals(client.getName(), clientDtoReturned.getName());
        Assertions.assertEquals(client.getEmail(), clientDtoReturned.getEmail());
        Assertions.assertEquals(client.getAddress(), clientDtoReturned.getAddress());
    }

    @Test
    void getClientByIdThrowsClientNotFound() {

        Mockito.when(clientRepository.findById(ArgumentMatchers.anyInt())).thenThrow(ClientNotFoundException.class);

        Assertions.assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(ArgumentMatchers.anyInt()));
    }

    @Test
     void addClientTest() {
        ShopCart shopCart = new ShopCart();
        FavoriteItem favoriteItem = new FavoriteItem();
        Client client = new Client();
        client.setName("catalin");
        client.setEmail("catalin@gmail");
        client.setAddress("bucuresti");
        client.setShopCart(shopCart);
        client.setFavoriteItem(favoriteItem);

        Mockito.when(clientRepository.save(any(Client.class))).thenReturn(client);
        ClientDto clientAdded = clientService.addClient(ClientMapper.toDto(client), "password");

        Assertions.assertNotNull(clientAdded);
        Assertions.assertEquals(client.getName(), clientAdded.getName());
        Assertions.assertEquals(client.getAddress(), clientAdded.getAddress());
        Assertions.assertEquals(client.getEmail(), clientAdded.getEmail());
        Assertions.assertEquals(client.getId(), clientAdded.getId());
    }

    @Test
     void updateClientTest() {
        ShopCart shopCart = new ShopCart();
        FavoriteItem favoriteItem = new FavoriteItem();
        shopCart.setId(1);
        favoriteItem.setId(1);
        Client client = new Client(1, "catalin", "catalin@gmail", "bucuresti", "123", "", shopCart, favoriteItem);
        ClientDto clientEdit = new ClientDto();
        ShopCartDto shopCartDto = new ShopCartDto();
        shopCartDto.setId(1);
        FavoriteItemDto favoriteItemDto = new FavoriteItemDto();
        favoriteItemDto.setId(1);
        clientEdit.setName("spatarelu");
        clientEdit.setAddress("bucuresti");
        clientEdit.setEmail("spatarelu@gmail.com");
        clientEdit.setId(client.getId());
        clientEdit.setShopCartDto(shopCartDto);
        clientEdit.setFavoriteItemDto(favoriteItemDto);

        Mockito.when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.save(any())).thenReturn(ClientMapper.fromDto(clientEdit));

        ClientDto clientUpdated = clientService.updateClient(1, clientEdit);

        Assertions.assertEquals(clientEdit.getName(), clientUpdated.getName());
        Assertions.assertEquals(clientEdit.getAddress(), clientUpdated.getAddress());
        Assertions.assertEquals(clientEdit.getEmail(), clientUpdated.getEmail());
        Assertions.assertEquals(client.getId(), clientUpdated.getId());
        Assertions.assertNotNull(clientUpdated);
    }

    @Test
    void deleteClientByIdTest() {
        ShopCart shopCart = new ShopCart();
        FavoriteItem favoriteItem = new FavoriteItem();
        shopCart.setId(1);
        favoriteItem.setId(1);
        Client client = new Client();
        client.setId(1);
        client.setShopCart(shopCart);
        client.setFavoriteItem(favoriteItem);

        Mockito.when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));


        clientService.deleteClientById(client.getId());

        Mockito.verify(clientRepository).deleteById(client.getId());
        Mockito.verify(favoriteItemRepository).deleteById(client.getFavoriteItem().getId());
        Mockito.verify(shopCartRepository).deleteById(client.getShopCart().getId());
    }

    @Test
    void deleteClientByIdThrowExceptionTest() {

        Mockito.when(clientRepository.findById(ArgumentMatchers.anyInt())).thenThrow(ClientNotFoundException.class);

        Assertions.assertThrows(ClientNotFoundException.class, () -> clientService.deleteClientById(ArgumentMatchers.anyInt()));
    }

}
