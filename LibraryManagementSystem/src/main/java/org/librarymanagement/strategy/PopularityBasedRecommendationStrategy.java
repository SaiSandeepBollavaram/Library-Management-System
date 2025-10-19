package org.librarymanagement.strategy;

import org.librarymanagement.mainentities.Book;
import org.librarymanagement.mainentities.LendingRecord;
import org.librarymanagement.mainentities.Patron;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Recommendation strategy based on book popularity.
 * Recommends books that are frequently borrowed by all patrons.
 */
public class PopularityBasedRecommendationStrategy implements RecommendationStrategy {
    
    private final Map<String, Integer> bookPopularityCache;
    
    public PopularityBasedRecommendationStrategy() {
        this.bookPopularityCache = new HashMap<>();
    }
    
    /**
     * Update popularity cache with borrowing data from all patrons
     */
    public void updatePopularityData(List<Patron> allPatrons) {
        bookPopularityCache.clear();
        
        for (Patron patron : allPatrons) {
            for (LendingRecord record : patron.getBorrowingHistory()) {
                bookPopularityCache.merge(record.getIsbn(), 1, Integer::sum);
            }
        }
    }
    
    @Override
    public List<Book> recommend(Patron patron, List<Book> allBooks, int limit) {
        // Get books patron has already borrowed
        Set<String> borrowedIsbns = patron.getBorrowingHistory().stream()
                .map(LendingRecord::getIsbn)
                .collect(Collectors.toSet());
        
        // Sort books by popularity (borrow count) and filter out already borrowed books
        return allBooks.stream()
                .filter(Book::isAvailable)
                .filter(book -> !borrowedIsbns.contains(book.getIsbn()))
                .sorted((b1, b2) -> {
                    int count1 = bookPopularityCache.getOrDefault(b1.getIsbn(), 0);
                    int count2 = bookPopularityCache.getOrDefault(b2.getIsbn(), 0);
                    // Sort by popularity descending, then by publication year descending
                    if (count1 != count2) {
                        return Integer.compare(count2, count1);
                    }
                    return Integer.compare(b2.getPublicationYear(), b1.getPublicationYear());
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
}
