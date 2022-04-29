package com.example.final_project.controller;

import com.example.final_project.dto.FavoriteItemDto;
import com.example.final_project.service.FavoriteItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoriteItems")
public class FavoriteItemController {

    private FavoriteItemService favoriteItemService;

    @Autowired
    public FavoriteItemController(FavoriteItemService favoriteItemService) {
        this.favoriteItemService = favoriteItemService;
    }

    @GetMapping()
    public ResponseEntity<List<FavoriteItemDto>> getAll() {
        return new ResponseEntity<>(favoriteItemService.getAllFavoriteList(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteItemDto> getById(@PathVariable("id") int id) {
        return new ResponseEntity<>(favoriteItemService.getFavoriteItemById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FavoriteItemDto> updateFavoriteItem(@PathVariable("id") int id, @RequestBody FavoriteItemDto favoriteItemDto) {
        return new ResponseEntity<>(favoriteItemService.updateFavoriteItem(id, favoriteItemDto), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<FavoriteItemDto> addFavoriteItem(@RequestBody FavoriteItemDto favoriteItemDto) {
        return new ResponseEntity<>(favoriteItemService.addFavoriteItem(favoriteItemDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteFavoriteItem(@PathVariable("id") int id) {
        favoriteItemService.deleteFavoriteItemById(id);
    }
}
