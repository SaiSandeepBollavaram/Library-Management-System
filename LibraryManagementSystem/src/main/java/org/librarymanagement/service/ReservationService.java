package org.librarymanagement.service;

import org.librarymanagement.mainentities.Book;
import org.librarymanagement.mainentities.Patron;
import org.librarymanagement.mainentities.Reservation;
import org.librarymanagement.mainentities.ReservationStatus;
import org.librarymanagement.observer.ReservationObserver;
import org.librarymanagement.repository.BookRepository;
import org.librarymanagement.repository.PatronRepository;
import org.librarymanagement.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing book reservations.
 * Implements Observer pattern to notify patrons when reserved books become available.
 */
public class ReservationService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private static final int RESERVATION_HOLD_DAYS = 3; // Days to hold a book for pickup
    
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final List<ReservationObserver> observers;
    
    public ReservationService(ReservationRepository reservationRepository, 
                            BookRepository bookRepository,
                            PatronRepository patronRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.observers = new ArrayList<>();
    }
    
    /**
     * Add an observer for reservation events
     */
    public void addObserver(ReservationObserver observer) {
        observers.add(observer);
        logger.info("Observer added: {}", observer.getClass().getSimpleName());
    }
    
    /**
     * Remove an observer
     */
    public void removeObserver(ReservationObserver observer) {
        observers.remove(observer);
        logger.info("Observer removed: {}", observer.getClass().getSimpleName());
    }
    
    /**
     * Notify all observers
     */
    private void notifyObservers(String event, Object data) {
        for (ReservationObserver observer : observers) {
            observer.update(event, data);
        }
    }
    
    /**
     * Create a reservation for a book
     */
    public Reservation createReservation(String isbn, String patronId) {
        logger.info("Creating reservation for book {} by patron {}", isbn, patronId);
        
        // Validate book exists
        Optional<Book> bookOpt = bookRepository.findByIsbn(isbn);
        if (bookOpt.isEmpty()) {
            logger.error("Book not found: {}", isbn);
            throw new IllegalArgumentException("Book not found: " + isbn);
        }
        
        // Validate patron exists
        Optional<Patron> patronOpt = patronRepository.findById(patronId);
        if (patronOpt.isEmpty()) {
            logger.error("Patron not found: {}", patronId);
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }
        
        Book book = bookOpt.get();
        Patron patron = patronOpt.get();
        
        // Check if book is available - if so, patron should borrow directly
        if (book.isAvailable()) {
            logger.warn("Book {} is available - patron should borrow directly", isbn);
            throw new IllegalArgumentException("Book is currently available. Please borrow directly.");
        }
        
        // Check if patron already has an active reservation for this book
        List<Reservation> patronReservations = reservationRepository.findByPatronId(patronId);
        boolean alreadyReserved = patronReservations.stream()
                .anyMatch(r -> r.getIsbn().equals(isbn) && isActiveStatus(r));
        
        if (alreadyReserved) {
            logger.warn("Patron {} already has a reservation for book {}", patronId, isbn);
            throw new IllegalArgumentException("You already have an active reservation for this book");
        }
        
        // Create reservation
        Reservation reservation = new Reservation(isbn, patronId);
        
        // Set queue position
        List<Reservation> existingReservations = reservationRepository.findActiveReservationsByIsbn(isbn);
        reservation.setQueuePosition(existingReservations.size() + 1);
        
        // Save reservation
        reservationRepository.save(reservation);
        
        logger.info("Reservation created: {} for book {} at position {}", 
                reservation.getReservationId(), isbn, reservation.getQueuePosition());
        
        // Notify observers
        notifyObservers("RESERVATION_CREATED", reservation);
        
        return reservation;
    }
    
    /**
     * Process book return - check if there are any reservations and notify next patron in queue
     */
    public void processBookReturn(String isbn) {
        logger.info("Processing book return for reservations: {}", isbn);
        
        List<Reservation> activeReservations = reservationRepository.findActiveReservationsByIsbn(isbn);
        
        if (!activeReservations.isEmpty()) {
            // Get the first reservation in queue
            Reservation nextReservation = activeReservations.get(0);
            
            // Mark as available and set expiry date
            nextReservation.setStatus(getReservationStatusEnum("AVAILABLE"));
            nextReservation.setExpiryDate(LocalDateTime.now().plusDays(RESERVATION_HOLD_DAYS));
            nextReservation.setNotificationSentDate(LocalDateTime.now());
            reservationRepository.update(nextReservation);
            
            // Get patron details
            Optional<Patron> patronOpt = patronRepository.findById(nextReservation.getPatronId());
            if (patronOpt.isPresent()) {
                Patron patron = patronOpt.get();
                logger.info("Notifying patron {} that book {} is ready for pickup", 
                        patron.getName(), isbn);
                
                // Notify observers (email, SMS, etc.)
                notifyObservers("RESERVATION_READY", new Object[]{nextReservation, patron});
            }
            
            // Update queue positions for remaining reservations
            for (int i = 1; i < activeReservations.size(); i++) {
                Reservation res = activeReservations.get(i);
                res.setQueuePosition(i);
                reservationRepository.update(res);
            }
        }
    }
    
    /**
     * Cancel a reservation
     */
    public void cancelReservation(String reservationId) {
        logger.info("Cancelling reservation: {}", reservationId);
        
        Optional<Reservation> resOpt = reservationRepository.findById(reservationId);
        if (resOpt.isEmpty()) {
            logger.error("Reservation not found: {}", reservationId);
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        }
        
        Reservation reservation = resOpt.get();
        reservation.setStatus(getReservationStatusEnum("CANCELLED"));
        reservationRepository.update(reservation);
        
        logger.info("Reservation cancelled: {}", reservationId);
        
        // Update queue positions for remaining reservations
        List<Reservation> remainingReservations = 
                reservationRepository.findActiveReservationsByIsbn(reservation.getIsbn());
        for (int i = 0; i < remainingReservations.size(); i++) {
            Reservation res = remainingReservations.get(i);
            res.setQueuePosition(i + 1);
            reservationRepository.update(res);
        }
    }
    
    /**
     * Get all reservations for a patron
     */
    public List<Reservation> getPatronReservations(String patronId) {
        return reservationRepository.findByPatronId(patronId);
    }
    
    /**
     * Get active reservations for a book
     */
    public List<Reservation> getBookReservations(String isbn) {
        return reservationRepository.findActiveReservationsByIsbn(isbn);
    }
    
    /**
     * Helper method to check if reservation status is active
     */
    private boolean isActiveStatus(Reservation reservation) {
        String status = reservation.getStatus().name();
        return "ACTIVE".equals(status) || "AVAILABLE".equals(status);
    }
    
    /**
     * Helper method to get ReservationStatus enum
     */
    private ReservationStatus getReservationStatusEnum(String status) {
        return ReservationStatus.valueOf(status);
    }
}
