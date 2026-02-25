package org.skypro.skyshop.model.basket;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@SessionScope
public class ProductBasket {

    // id продукта -> количество
    private final Map<UUID, Integer> products = new HashMap<>();

    // Добавление товара в корзину по id
    public void addProduct(UUID id) {
        products.merge(id, 1, Integer::sum);
    }

    // Получение всех товаров в корзине (немодифицируемая мапа)
    public Map<UUID, Integer> getProducts() {
        return Collections.unmodifiableMap(products);
    }
}