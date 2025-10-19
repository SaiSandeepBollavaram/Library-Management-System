package org.librarymanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.librarymanagement.mainentities.Book;
import org.librarymanagement.mainentities.BookStatus;
import org.librarymanagement.strategy.SearchStrategy;
import org.librarymanagement.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book) {
        try{
            bookRepository.add(book);
            logger.info("Book added successfully: {}", book.getIsbn());
        }catch (Exception e){
            logger.error("Error adding book: {}", book.getIsbn(), e);
            throw e;
        }
    }

    public void modifyBook(Book book) {
        try{
            bookRepository.modify(book);
            logger.info("Book modified successfully: {}", book.getIsbn());
        }catch (Exception e){
            logger.error("Error modifying book: {}", book.getIsbn(), e);
            throw e;
        }
    }

    public boolean removeBook(String isbn) {
        try{
            boolean removed = bookRepository.delete(isbn);
            if (removed) {
                logger.info("Book removed successfully: {}", isbn);
            } else {
                logger.warn("Book not found for removal: {}", isbn);
            }
            return removed;
        }catch (Exception e){
            logger.error("Error removing book: {}", isbn, e);
            throw e;
        }
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooks(SearchStrategy searchStrategy, String query) {
        logger.info("Searching books with query: {}", query);
        List<Book> allBooks = getAllBooks();
        return searchStrategy.search(allBooks, query);
    }

    public List<Book> getAvailableBooks() {
        return getAllBooks().stream().filter(book -> book.getStatus().equals(BookStatus.AVAILABLE)).collect(Collectors.toList());
    }
}
