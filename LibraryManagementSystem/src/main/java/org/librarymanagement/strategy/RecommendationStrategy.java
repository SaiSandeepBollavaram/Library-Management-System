package org.librarymanagement.strategy;

import org.librarymanagement.mainentities.Book;
import org.librarymanagement.mainentities.Patron;
import java.util.List;

/**
 * Strategy interface for book recommendation algorithms.
 * Implements Strategy Pattern to allow different recommendation approaches.
 */
public interface RecommendationStrategy {
    
    /**
     * Generate book recommendations for a patron
     * 
     * @param patron The patron to generate recommendations for
     * @param allBooks All available books in the library
     * @param limit Maximum number of recommendations to return
     * @return List of recommended books
     */
    List<Book> recommend(Patron patron, List<Book> allBooks, int limit);
}
