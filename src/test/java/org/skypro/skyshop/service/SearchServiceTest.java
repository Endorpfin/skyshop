package org.skypro.skyshop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    @Test
    @DisplayName("Поиск при пустом хранилище — возвращается пустой список")
    void searchWhenStorageEmpty() {
        when(storageService.getAllSearchable()).thenReturn(List.of());

        Collection<SearchResult> results = searchService.search("Молоко");

        assertTrue(results.isEmpty(), "Результаты должны быть пустыми при пустом хранилище");
    }

    @Test
    @DisplayName("Поиск, когда объекты есть, но ни один не подходит — пустой результат")
    void searchWhenNoMatchingObjects() {
        Searchable product = new SimpleProduct(UUID.randomUUID(), "Хлеб", 30);
        Searchable article = new Article(
                UUID.randomUUID(),
                "Рецепты с сыром",
                "Сыр отлично подходит для пасты и салатов."
        );

        when(storageService.getAllSearchable()).thenReturn(List.of(product, article));

        Collection<SearchResult> results = searchService.search("Молоко");

        assertTrue(results.isEmpty(), "Результаты должны быть пустыми, если нет совпадений");
    }

    @Test
    @DisplayName("Поиск, когда есть подходящие объекты — возвращаются корректные SearchResult")
    void searchWhenMatchingObjects() {
        UUID milkId = UUID.randomUUID();
        Searchable milkProduct = new SimpleProduct(milkId, "Молоко", 50);
        Searchable milkArticle = new Article(
                UUID.randomUUID(),
                "Как выбрать молоко",
                "Молоко бывает разной жирности и от разных производителей."
        );

        when(storageService.getAllSearchable()).thenReturn(List.of(milkProduct, milkArticle));

        Collection<SearchResult> results = searchService.search("Молоко");

        assertEquals(2, results.size(), "Должно быть два результата поиска");

        List<SearchResult> list = results.stream().toList();

        List<String> names = list.stream().map(SearchResult::getName).toList();
        List<String> types = list.stream().map(SearchResult::getContentType).toList();

        assertTrue(names.contains("Молоко"));
        assertTrue(names.contains("Как выбрать молоко"));

        assertTrue(types.contains("PRODUCT"));
        assertTrue(types.contains("ARTICLE"));
    }

    @Test
    @DisplayName("Поиск с пустым или null шаблоном — пустой список и без обращений к storageService")
    void searchWithBlankOrNullPattern() {
        Collection<SearchResult> results1 = searchService.search("");
        Collection<SearchResult> results2 = searchService.search("   ");
        Collection<SearchResult> results3 = searchService.search(null);

        assertTrue(results1.isEmpty());
        assertTrue(results2.isEmpty());
        assertTrue(results3.isEmpty());
    }
}