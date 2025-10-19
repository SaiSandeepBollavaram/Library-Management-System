package org.librarymanagement.strategy;

import org.librarymanagement.mainentities.Book;

import java.util.List;

public interface SearchStrategy {
    List<Book> search(List<Book> books, String query);
}
