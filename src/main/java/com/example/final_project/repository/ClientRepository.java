package com.example.final_project.repository;

import com.example.final_project.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByEmailAndPassword(String email, String password);

    Client findByEmail(String email);
}
