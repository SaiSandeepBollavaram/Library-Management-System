package org.librarymanagement.service;

import org.librarymanagement.mainentities.Book;
import org.librarymanagement.mainentities.Patron;
import org.librarymanagement.repository.BookRepository;
import org.librarymanagement.repository.PatronRepository;
import org.librarymanagement.strategy.RecommendationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Service class for generating book recommendations.
 * Uses Strategy Pattern to support different recommendation algorithms.
 * Follows Dependency Inversion Principle - depends on abstractions (interfaces).
 */
public class RecommendationService {
    
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);
    private static final int DEFAULT_RECOMMENDATION_LIMIT = 5;
    
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private RecommendationStrategy strategy;
    
    public RecommendationService(BookRepository bookRepository, 
                                PatronRepository patronRepository,
                                RecommendationStrategy defaultStrategy) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.strategy = defaultStrategy;
    }
    
    /**
     * Set the recommendation strategy
     */
    public void setStrategy(RecommendationStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
        logger.info("Recommendation strategy changed to: {}", strategy.getClass().getSimpleName());
    }
    
    /**
     * Get current recommendation strategy
     */
    public RecommendationStrategy getStrategy() {
        return strategy;
    }
    
    /**
     * Generate recommendations for a patron using the current strategy
     */
    public List<Book> getRecommendations(String patronId) {
        return getRecommendations(patronId, DEFAULT_RECOMMENDATION_LIMIT);
    }
    
    /**
     * Generate recommendations for a patron with a specific limit
     */
    public List<Book> getRecommendations(String patronId, int limit) {
        logger.info("Generating recommendations for patron: {} with limit: {}", patronId, limit);
        
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive");
        }
        
        // Validate patron exists
        Optional<Patron> patronOpt = patronRepository.findById(patronId);
        if (patronOpt.isEmpty()) {
            logger.error("Patron not found: {}", patronId);
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }
        
        Patron patron = patronOpt.get();
        List<Book> allBooks = bookRepository.findAll();
        
        // Generate recommendations using current strategy
        List<Book> recommendations = strategy.recommend(patron, allBooks, limit);
        
        logger.info("Generated {} recommendations for patron {}", recommendations.size(), patronId);
        return recommendations;
    }
    
    /**
     * Generate recommendations using a specific strategy (without changing the default)
     */
    public List<Book> getRecommendationsWithStrategy(String patronId, 
                                                     RecommendationStrategy customStrategy, 
                                                     int limit) {
        logger.info("Generating recommendations for patron: {} with custom strategy: {}", 
                patronId, customStrategy.getClass().getSimpleName());
        
        Optional<Patron> patronOpt = patronRepository.findById(patronId);
        if (patronOpt.isEmpty()) {
            logger.error("Patron not found: {}", patronId);
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }
        
        Patron patron = patronOpt.get();
        List<Book> allBooks = bookRepository.findAll();
        
        return customStrategy.recommend(patron, allBooks, limit);
    }
}
