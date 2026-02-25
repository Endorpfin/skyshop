package org.skypro.skyshop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private ProductBasket productBasket;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private BasketService basketService;

    @Test
    @DisplayName("Добавление несуществующего товара вызывает NoSuchProductException и не трогает корзину")
    void addNonExistingProductThrowsException() {
        UUID id = UUID.randomUUID();
        when(storageService.getProductById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchProductException.class, () -> basketService.addProduct(id));

        verify(productBasket, never()).addProduct(any());
    }

    @Test
    @DisplayName("Добавление существующего товара вызывает addProduct у ProductBasket")
    void addExistingProductCallsBasketAddProduct() {
        UUID id = UUID.randomUUID();
        Product product = new SimpleProduct(id, "Молоко", 50);

        when(storageService.getProductById(id)).thenReturn(Optional.of(product));

        basketService.addProduct(id);

        verify(productBasket, times(1)).addProduct(id);
    }

    @Test
    @DisplayName("getUserBasket возвращает пустую корзину, если ProductBasket пуст")
    void getUserBasketReturnsEmptyWhenBasketEmpty() {
        when(productBasket.getProducts()).thenReturn(Map.of());

        UserBasket userBasket = basketService.getUserBasket();

        assertNotNull(userBasket);
        assertTrue(userBasket.getItems().isEmpty());
        assertEquals(0, userBasket.getTotal());
    }

    @Test
    @DisplayName("getUserBasket возвращает корректную корзину, если в ProductBasket есть товары")
    void getUserBasketReturnsProperBasketWhenBasketHasItems() {
        UUID milkId = UUID.randomUUID();
        UUID breadId = UUID.randomUUID();

        // В корзине: 2 молока и 1 хлеб
        when(productBasket.getProducts()).thenReturn(
                Map.of(
                        milkId, 2,
                        breadId, 1
                )
        );

        Product milk = new SimpleProduct(milkId, "Молоко", 50);
        Product bread = new SimpleProduct(breadId, "Хлеб", 30);

        when(storageService.getProductById(milkId)).thenReturn(Optional.of(milk));
        when(storageService.getProductById(breadId)).thenReturn(Optional.of(bread));

        UserBasket userBasket = basketService.getUserBasket();

        assertNotNull(userBasket);
        List<BasketItem> items = userBasket.getItems();
        assertEquals(2, items.size());

        // Проверяем итоговую сумму: 2*50 + 1*30 = 130
        assertEquals(130, userBasket.getTotal());

        // Проверяем, что в корзине есть Молоко и Хлеб с правильным количеством
        Map<String, Integer> nameToQty = items.stream()
                .collect(java.util.stream.Collectors.toMap(
                        item -> item.getProduct().getName(),
                        BasketItem::getQuantity
                ));

        assertEquals(2, nameToQty.get("Молоко"));
        assertEquals(1, nameToQty.get("Хлеб"));
    }
}