package org.librarymanagement.strategy;

import org.librarymanagement.mainentities.Book;

import java.util.List;
import java.util.stream.Collectors;

public class ISBNSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        String searchQuery = query.trim();
        return books.stream()
                .filter(book -> book.getIsbn().equals(searchQuery))
                .collect(Collectors.toList());
    }
}
