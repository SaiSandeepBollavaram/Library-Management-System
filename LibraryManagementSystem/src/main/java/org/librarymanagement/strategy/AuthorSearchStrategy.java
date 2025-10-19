package org.librarymanagement.strategy;

import org.librarymanagement.mainentities.Book;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        String searchQuery = query.toLowerCase().trim();
        return books.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(searchQuery))
                .collect(Collectors.toList());
    }
}
