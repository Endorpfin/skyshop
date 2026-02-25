package org.skypro.skyshop.service;

import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class SearchService {

    private final StorageService storageService;

    public SearchService(StorageService storageService) {
        this.storageService = storageService;
    }

    public Collection<SearchResult> search(String pattern) {
        if (pattern == null || pattern.isBlank()) {
            return List.of();
        }

        return storageService.getAllSearchable().stream()
                .filter(item -> item.getSearchTerm().contains(pattern))
                .map(SearchResult::fromSearchable)
                .toList(); // Java 17
    }
}