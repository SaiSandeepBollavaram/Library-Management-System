package org.librarymanagement.repository;

import org.librarymanagement.mainentities.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    void add(Book book);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();
    void modify(Book book);
    boolean delete(String isbn);
}
