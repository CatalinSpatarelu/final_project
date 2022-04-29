package com.example.final_project.controller;

import com.example.final_project.dto.ShopCartDto;
import com.example.final_project.service.ShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopCarts")
public class ShopCartController {

    private ShopCartService shopCartService;

    @Autowired
    public ShopCartController(ShopCartService shopCartService) {
        this.shopCartService = shopCartService;
    }

    @GetMapping()
    public ResponseEntity<List<ShopCartDto>> getAll() {
        return new ResponseEntity<>(shopCartService.getAllShopCartList(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopCartDto> getById(@PathVariable("id") int id) {
        return new ResponseEntity<>(shopCartService.getShopCartById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShopCartDto> updateShopCart(@PathVariable("id") int id, @RequestBody ShopCartDto shopCartDto) {
        return new ResponseEntity<>(shopCartService.updateShopCart(id, shopCartDto), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public void deleteShopCartById(@PathVariable("id") int id) {
        shopCartService.deleteShopCartById(id);
    }
}
