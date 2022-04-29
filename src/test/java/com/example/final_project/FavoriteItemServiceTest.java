package com.example.final_project;

import com.example.final_project.dto.FavoriteItemDto;
import com.example.final_project.exceptions.FavoriteItemNotFoundException;
import com.example.final_project.mapper.FavoriteItemMapper;
import com.example.final_project.model.FavoriteItem;
import com.example.final_project.repository.FavoriteItemRepository;
import com.example.final_project.service.FavoriteItemService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class FavoriteItemServiceTest {

    @Mock
    private FavoriteItemRepository favoriteItemRepository;

    @InjectMocks
    private FavoriteItemService favoriteItemService;

    @Test
    void getAllFavoriteListTest() {
        FavoriteItem favoriteItemOne = new FavoriteItem();
        FavoriteItem favoriteItemTwo = new FavoriteItem();
        favoriteItemOne.setProducts(Collections.emptySet());
        favoriteItemTwo.setProducts(Collections.emptySet());
        List<FavoriteItem> expectedList = List.of(favoriteItemOne, favoriteItemTwo);
        Mockito.when(favoriteItemRepository.findAll()).thenReturn(expectedList);
        List<FavoriteItemDto> actualList = favoriteItemService.getAllFavoriteList();
        List<FavoriteItem> actualListFavoriteItem = actualList
                .stream()
                .map(favoriteItemDto -> FavoriteItemMapper.fromDto(favoriteItemDto))
                .collect(Collectors.toList());
        Assertions.assertNotNull(actualList);
        Assertions.assertEquals(2, actualList.size());
        Assertions.assertEquals(expectedList, actualListFavoriteItem);
    }

    @Test
    void getFavoriteItemById() {
        FavoriteItem favoriteItem = new FavoriteItem();
        favoriteItem.setId(1);
        Mockito.when(favoriteItemRepository.findById(favoriteItem.getId())).thenReturn(Optional.of(favoriteItem));
        FavoriteItemDto favoriteItemExpected = favoriteItemService.getFavoriteItemById(favoriteItem.getId());

        Assertions.assertNotNull(favoriteItemExpected);
        Assertions.assertEquals(favoriteItemExpected, FavoriteItemMapper.toDto(favoriteItem));
    }

    @Test
    void getFavoriteItemByIdThrowsExceptionTest() {
        Mockito.when(favoriteItemRepository.findById(ArgumentMatchers.anyInt())).thenThrow(FavoriteItemNotFoundException.class);

        Assertions.assertThrows(FavoriteItemNotFoundException.class, () -> favoriteItemService.getFavoriteItemById(ArgumentMatchers.anyInt()));
    }

    @Test
    void updateFavoriteItemThrowsException() {
        FavoriteItem favoriteItem = new FavoriteItem();
        favoriteItem.setId(1);
        Mockito.when(favoriteItemRepository.findById(favoriteItem.getId())).thenThrow(FavoriteItemNotFoundException.class);

        Assertions.assertThrows(FavoriteItemNotFoundException.class, () -> favoriteItemService.updateFavoriteItem(favoriteItem.getId(), FavoriteItemMapper.toDto(favoriteItem)));
    }

    @Test
    void deleteFavoriteItemByIdTest() {

        FavoriteItem favoriteItem = new FavoriteItem();
        favoriteItem.setId(1);

        Mockito.when(favoriteItemRepository.findById(favoriteItem.getId())).thenReturn(Optional.of(favoriteItem));

        favoriteItemService.deleteFavoriteItemById(favoriteItem.getId());

        Mockito.verify(favoriteItemRepository).deleteById(favoriteItem.getId());
    }

    @Test
    void deleteFavoriteItemByIdThrowsExceptionTest() {

        Mockito.when(favoriteItemRepository.findById(ArgumentMatchers.anyInt())).thenThrow(FavoriteItemNotFoundException.class);

        Assertions.assertThrows(FavoriteItemNotFoundException.class, () -> favoriteItemService.deleteFavoriteItemById(ArgumentMatchers.anyInt()));
    }
}
