package com.example.final_project;

import com.example.final_project.model.Client;
import com.example.final_project.repository.ClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClientTest {

    @Autowired
    private ClientRepository clientRepository;

  //  @BeforeEach
  //  public void cleanDatabase() {
  //      clientRepository.deleteAll();
  //  }

   @AfterEach
   public void cleanDatabaseAfter() {
       clientRepository.deleteAll();
   }

    @Test
    public void test(){
        Client client = new Client();
        client.setName("Catalin");
        client.setAddress("Theodor Pallady");
        client.setEmail("catalin@gmail.com");
        Client savedClient = clientRepository.save(client);

        Assertions.assertNotNull(savedClient);
        Assertions.assertEquals("Catalin",savedClient.getName());
        Assertions.assertEquals("Theodor Pallady", savedClient.getAddress());
        Assertions.assertEquals("catalin@gmail.com",savedClient.getEmail());
    }
}
