package com.example.final_project;

import com.example.final_project.dto.ClientDto;
import com.example.final_project.repository.ClientRepository;
import com.example.final_project.repository.FavoriteItemRepository;
import com.example.final_project.repository.ShopCartRepository;
import com.example.final_project.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ShopCartRepository shopCartRepository;
    @Autowired
    private FavoriteItemRepository favoriteItemRepository;

    @BeforeEach
    public void cleanDatabase() {
        clientRepository.deleteAll();
        shopCartRepository.deleteAll();
        favoriteItemRepository.deleteAll();
    }

    @Test
    void getAllClientsWithoutClientsInDataBaseTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/clients")
                        .header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    void getAllClientsTest() throws Exception {
        ClientDto clientOne = new ClientDto();
        clientOne.setName("catalin");
        clientOne.setEmail("catalin@gmail.com");
        clientOne.setAddress("address");
        ClientDto clientTwo = new ClientDto();
        clientTwo.setName("ioan");
        clientTwo.setEmail("ioan@gmail.com");
        clientTwo.setAddress("address");

        ClientDto clientOneAdded = clientService.addClient(clientOne, "password");
        ClientDto clientTwoAdded = clientService.addClient(clientTwo, "mypassword");

        List<ClientDto> listOfClients = List.of(clientOneAdded, clientTwoAdded);

        mockMvc.perform(MockMvcRequestBuilders.get("/clients")
                        .header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith((MediaType.APPLICATION_JSON)))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(listOfClients)));

    }

    @Test
    void getByIdTest() throws Exception {
        ClientDto client = new ClientDto();
        client.setAddress("bucuresti");
        client.setEmail("catalin@gmail");
        client.setName("catalin");
        ClientDto clientAdded = clientService.addClient(client, "password");
        mockMvc.perform(MockMvcRequestBuilders.get("/clients/" + clientAdded.getId())
                        .header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(clientAdded)));
    }

    @Test
    void getByIdThrowClientNotFoundTest() throws Exception {

        mockMvc.perform((MockMvcRequestBuilders.get("/clients/" + ArgumentMatchers.anyInt())
                        .header("Authorization", "Basic dGVzdDp0ZXN0")))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Client not found"));

    }

    @Test
    void updateClientTest() throws Exception {
        ClientDto client = new ClientDto();

        client.setAddress("bucuresti");
        client.setEmail("catalin@gmail");
        client.setName("catalin");

        ClientDto clientToUpdate = new ClientDto();
        clientToUpdate.setName("spatarelu");
        clientToUpdate.setAddress("bucuresti");
        clientToUpdate.setEmail("spatarelu@gmail.com");
        ClientDto clientDto = clientService.addClient(client, "password");
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/clients/" + clientDto.getId())
                        .header("Authorization", "Basic dGVzdDp0ZXN0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientToUpdate)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        ClientDto clientReturned = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ClientDto.class);

        Assertions.assertNotNull(clientReturned);
        Assertions.assertEquals(clientDto.getId(), clientReturned.getId());
        Assertions.assertEquals("spatarelu", clientReturned.getName());
        Assertions.assertEquals("spatarelu@gmail.com", clientReturned.getEmail());
        Assertions.assertEquals("bucuresti", clientReturned.getAddress());

    }

    @Test
    void updateClientThrowExceptionTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/clients/" + ArgumentMatchers.anyInt())
                        .header("Authorization", "Basic dGVzdDp0ZXN0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ClientDto())))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Client not found"));
    }

    @Test
    void createClientTest() throws Exception {
        ClientDto clientDto = new ClientDto();
        clientDto.setAddress("bucuresti");
        clientDto.setEmail("catalin@gmail");
        clientDto.setName("catalin");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("password", "passowrd");

        ResultActions resultActions = mockMvc.perform((MockMvcRequestBuilders.post("/clients")
                        .header("Authorization", "Basic dGVzdDp0ZXN0"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto))
                        .headers(httpHeaders))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        ClientDto clientReturned = objectMapper.readValue(resultActions.andReturn()
                .getResponse().getContentAsString(), ClientDto.class);

        Assertions.assertNotNull(clientReturned.getId());
        Assertions.assertNotNull(clientReturned.getShopCartDto());
        Assertions.assertNotNull(clientReturned.getFavoriteItemDto());
        Assertions.assertNotNull(clientReturned.getShopCartDto().getId());
        Assertions.assertNotNull(clientReturned.getFavoriteItemDto().getId());
        Assertions.assertEquals(clientDto.getAddress(), clientReturned.getAddress());
        Assertions.assertEquals(clientDto.getEmail(), clientReturned.getEmail());
        Assertions.assertEquals(clientDto.getName(), clientReturned.getName());
    }

    @Test
    public void deleteClientByIdTest() throws Exception {
        ClientDto clientDto = new ClientDto();
        clientDto.setAddress("bucuresti");
        clientDto.setEmail("catalin@gmail");
        clientDto.setName("catalin");
        ClientDto clientReturned = clientService.addClient(clientDto, "password");
        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/" + clientReturned.getId())
                        .header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(""));
        Assertions.assertEquals(Optional.empty(), clientRepository.findById(clientReturned.getId()));
        Assertions.assertEquals(Optional.empty(), shopCartRepository.findById(clientReturned.getShopCartDto().getId()));
        Assertions.assertEquals(Optional.empty(), favoriteItemRepository.findById(clientReturned.getFavoriteItemDto().getId()));
    }

    @Test
    public void deleteClientByIdThrowExceptionTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/" + ArgumentMatchers.anyInt())
                        .header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Client not found"));
    }
}
