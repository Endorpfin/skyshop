package org.skypro.skyshop.service;

import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BasketService {

    private final ProductBasket productBasket;
    private final StorageService storageService;

    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }

    // Добавление товара в корзину
    public void addProduct(UUID id) {
        Product product = storageService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Продукт с таким id не найден: " + id));
        // Если продукт существует, просто добавляем его id в корзину
        productBasket.addProduct(id);
    }

    // Получение корзины пользователя
    public UserBasket getUserBasket() {
        var productsMap = productBasket.getProducts(); // Map<UUID, Integer>

        List<BasketItem> items = productsMap.entrySet().stream()
                .map(entry -> {
                    UUID id = entry.getKey();
                    int quantity = entry.getValue();
                    Product product = storageService.getProductById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Продукт с таким id не найден: " + id));
                    return new BasketItem(product, quantity);
                })
                .toList();

        return new UserBasket(items);
    }
}