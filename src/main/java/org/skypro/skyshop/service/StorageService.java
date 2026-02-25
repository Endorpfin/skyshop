package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.FixPriceProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class StorageService {

    private final Map<UUID, Product> products = new HashMap<>();
    private final Map<UUID, Article> articles = new HashMap<>();

    public StorageService() {
        initTestData();
    }

    private void initTestData() {
        // Продукты
        Product milk = new SimpleProduct(UUID.randomUUID(), "Молоко", 50);
        Product bread = new SimpleProduct(UUID.randomUUID(), "Хлеб", 30);
        Product cheese = new DiscountedProduct(UUID.randomUUID(), "Сыр", 120, 25);
        Product tea = new DiscountedProduct(UUID.randomUUID(), "Чай", 80, 10);
        Product coffee = new FixPriceProduct(UUID.randomUUID(), "Кофе");
        Product water = new FixPriceProduct(UUID.randomUUID(), "Вода");

        products.put(milk.getId(), milk);
        products.put(bread.getId(), bread);
        products.put(cheese.getId(), cheese);
        products.put(tea.getId(), tea);
        products.put(coffee.getId(), coffee);
        products.put(water.getId(), water);

        // Статьи
        Article article1 = new Article(
                UUID.randomUUID(),
                "Как выбрать молоко",
                "Молоко бывает разной жирности и от разных производителей."
        );
        Article article2 = new Article(
                UUID.randomUUID(),
                "Рецепты с сыром",
                "Сыр отлично подходит для пасты и салатов."
        );
        Article article3 = new Article(
                UUID.randomUUID(),
                "Чай и кофе",
                "Чай и кофе — популярные напитки по утрам."
        );

        articles.put(article1.getId(), article1);
        articles.put(article2.getId(), article2);
        articles.put(article3.getId(), article3);
    }

    public Collection<Product> getAllProducts() {
        return products.values();
    }

    public Collection<Article> getAllArticles() {
        return articles.values();
    }

    public Collection<Searchable> getAllSearchable() {
        Collection<Searchable> result = new ArrayList<>();
        result.addAll(products.values());
        result.addAll(articles.values());
        return result;
    }

    // Новый метод: получить продукт по id
    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }
}