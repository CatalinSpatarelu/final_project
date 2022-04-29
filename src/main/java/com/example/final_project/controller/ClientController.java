package com.example.final_project.controller;

import com.example.final_project.dto.ClientDto;
import com.example.final_project.exceptions.PasswordNotPresentException;
import com.example.final_project.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/clients")
public class ClientController {


    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping()
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return new ResponseEntity<>(clientService.getAllClients(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable("id") int id) {
        return new ResponseEntity<>(clientService.getClientById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable("id") int id,
                                                  @Valid @RequestBody ClientDto clientDto) {
        return new ResponseEntity<>(clientService.updateClient(id, clientDto), HttpStatus.ACCEPTED);
    }

    @PostMapping()
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody ClientDto clientDto,
                                                  @RequestHeader Map<String, String> headers) {
        return new ResponseEntity<>(clientService.
                addClient(clientDto, getHeader(headers, "password")),
                HttpStatus.CREATED);
    }

    @GetMapping("/email")
    public ResponseEntity<ClientDto> getClientByEmail(@RequestHeader Map<String, String> headers) {
        return new ResponseEntity<>(clientService.
                getClientByEmailAndPassword(getHeader(headers, "email"),
                        getHeader(headers, "password")), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable("id") int id) {
        clientService.deleteClientById(id);
    }

    private String getHeader(Map<String, String> headers, String headerName) {
        if (headers.containsKey(headerName))
            return headers.get(headerName);
        else throw new PasswordNotPresentException();

    }
}
