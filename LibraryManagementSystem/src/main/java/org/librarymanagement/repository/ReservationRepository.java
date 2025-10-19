package org.librarymanagement.repository;

import org.librarymanagement.mainentities.Reservation;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Reservation entity operations.
 */
public interface ReservationRepository {
    
    /**
     * Save a reservation
     */
    void save(Reservation reservation);
    
    /**
     * Find a reservation by ID
     */
    Optional<Reservation> findById(String reservationId);
    
    /**
     * Find all active reservations for a specific book (ordered by queue position)
     */
    List<Reservation> findActiveReservationsByIsbn(String isbn);
    
    /**
     * Find all reservations for a specific patron
     */
    List<Reservation> findByPatronId(String patronId);
    
    /**
     * Update a reservation
     */
    void update(Reservation reservation);
    
    /**
     * Delete a reservation
     */
    void deleteById(String reservationId);
    
    /**
     * Get all reservations
     */
    List<Reservation> findAll();
}
