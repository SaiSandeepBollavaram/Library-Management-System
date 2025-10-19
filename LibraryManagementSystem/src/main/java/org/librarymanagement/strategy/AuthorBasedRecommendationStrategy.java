package org.librarymanagement.strategy;

import org.librarymanagement.mainentities.Book;
import org.librarymanagement.mainentities.LendingRecord;
import org.librarymanagement.mainentities.Patron;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Recommendation strategy based on authors patron has borrowed from.
 * Recommends books by favorite authors (authors with most borrowed books).
 */
public class AuthorBasedRecommendationStrategy implements RecommendationStrategy {
    
    @Override
    public List<Book> recommend(Patron patron, List<Book> allBooks, int limit) {
        // Get patron's borrowing history
        List<LendingRecord> history = patron.getBorrowingHistory();
        
        if (history.isEmpty()) {
            // No history - return popular books (by publication year)
            return allBooks.stream()
                    .filter(Book::isAvailable)
                    .sorted(Comparator.comparingInt(Book::getPublicationYear).reversed())
                    .limit(limit)
                    .collect(Collectors.toList());
        }
        
        // Find books patron has borrowed and extract their ISBNs
        Set<String> borrowedIsbns = history.stream()
                .map(LendingRecord::getIsbn)
                .collect(Collectors.toSet());
        
        // Create a map of ISBN to Book from all books
        Map<String, Book> isbnToBook = allBooks.stream()
                .collect(Collectors.toMap(Book::getIsbn, book -> book));
        
        // Count books by each author the patron has borrowed from
        Map<String, Long> authorFrequency = history.stream()
                .map(record -> isbnToBook.get(record.getIsbn()))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()));
        
        // Sort authors by frequency
        List<String> favoriteAuthors = authorFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        // Recommend available books by favorite authors that patron hasn't borrowed
        List<Book> recommendations = new ArrayList<>();
        
        for (String author : favoriteAuthors) {
            List<Book> authorBooks = allBooks.stream()
                    .filter(book -> book.getAuthor().equals(author))
                    .filter(Book::isAvailable)
                    .filter(book -> !borrowedIsbns.contains(book.getIsbn()))
                    .collect(Collectors.toList());
            
            recommendations.addAll(authorBooks);
            
            if (recommendations.size() >= limit) {
                break;
            }
        }
        
        // If we don't have enough recommendations, add other available books
        if (recommendations.size() < limit) {
            allBooks.stream()
                    .filter(Book::isAvailable)
                    .filter(book -> !borrowedIsbns.contains(book.getIsbn()))
                    .filter(book -> !recommendations.contains(book))
                    .limit(limit - recommendations.size())
                    .forEach(recommendations::add);
        }
        
        return recommendations.stream().limit(limit).collect(Collectors.toList());
    }
}
