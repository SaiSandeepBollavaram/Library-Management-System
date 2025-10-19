package org.librarymanagement.strategy;

import org.librarymanagement.mainentities.Book;

import java.util.List;
import java.util.stream.Collectors;

public class TitleSearchStrategy implements SearchStrategy {

    @Override
    public List<Book> search(List<Book> books, String query) {
        if(query == null || query.trim().isEmpty()){
            return List.of();
        }
        String searchQuery = query.trim().toLowerCase();
        return books.stream().filter(book -> book.getTitle().toLowerCase().contains(searchQuery)).collect(Collectors.toList());
    }
}
