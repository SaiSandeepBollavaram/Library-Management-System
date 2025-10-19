package org.librarymanagement.repository;

import org.librarymanagement.mainentities.Book;

import java.util.*;

public class InMemoryBookRepository implements BookRepository {

    private final Map<String, Book> bookRepository;

    public InMemoryBookRepository() {
        this.bookRepository = new HashMap<>();
    }

    @Override
    public void add(Book book) {
        if(book == null){
            throw new IllegalArgumentException("Book cannot be null");
        }
        if(bookRepository.containsKey(book.getIsbn())) {
            throw new IllegalStateException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        bookRepository.put(book.getIsbn(), book);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return  Optional.ofNullable(bookRepository.get(isbn));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(bookRepository.values());
    }

    @Override
    public void modify(Book book) {
        if(book == null){
            throw new IllegalArgumentException("Book cannot be null");
        }
        if(!bookRepository.containsKey(book.getIsbn())) {
            throw new IllegalStateException("Book with ISBN " + book.getIsbn() + " does not exist");
        }
        bookRepository.put(book.getIsbn(), book);
    }

    @Override
    public boolean delete(String isbn) {
        return bookRepository.remove(isbn) != null;
    }
}
