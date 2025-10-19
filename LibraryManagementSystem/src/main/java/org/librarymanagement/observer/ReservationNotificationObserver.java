package org.librarymanagement.observer;

import org.librarymanagement.mainentities.Patron;
import org.librarymanagement.mainentities.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Observer that handles reservation-specific notifications.
 * Notifies patrons when their reserved books become available.
 * Implements Observer Pattern for event-driven notifications.
 */
public class ReservationNotificationObserver implements ReservationObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(ReservationNotificationObserver.class);
    
    @Override
    public void update(String event, Object data) {
        switch (event) {
            case "RESERVATION_CREATED":
                handleReservationCreated((Reservation) data);
                break;
            case "RESERVATION_READY":
                handleReservationReady((Object[]) data);
                break;
            case "RESERVATION_CANCELLED":
                handleReservationCancelled((Reservation) data);
                break;
            default:
                // Ignore other events
                break;
        }
    }
    
    /**
     * Handle when a new reservation is created
     */
    private void handleReservationCreated(Reservation reservation) {
        logger.info("=== RESERVATION NOTIFICATION ===");
        logger.info("Reservation created for book: {}", reservation.getIsbn());
        logger.info("Patron ID: {}", reservation.getPatronId());
        logger.info("Queue Position: {}", reservation.getQueuePosition());
        logger.info("You will be notified when the book becomes available.");
        logger.info("===============================\n");
    }
    
    /**
     * Handle when a reserved book becomes available
     */
    private void handleReservationReady(Object[] data) {
        Reservation reservation = (Reservation) data[0];
        Patron patron = (Patron) data[1];
        
        logger.info("=== BOOK READY FOR PICKUP ===");
        logger.info("Dear {}, your reserved book is ready!", patron.getName());
        logger.info("Book ISBN: {}", reservation.getIsbn());
        logger.info("Patron: {} ({})", patron.getName(), patron.getEmail());
        logger.info("Reservation ID: {}", reservation.getReservationId());
        logger.info("Please pick up the book within 3 days.");
        logger.info("Expiry Date: {}", reservation.getExpiryDate());
        logger.info("============================\n");
        
        // In a real system, this would send an email/SMS to the patron
        sendEmailNotification(patron, reservation);
    }
    
    /**
     * Handle when a reservation is cancelled
     */
    private void handleReservationCancelled(Reservation reservation) {
        logger.info("=== RESERVATION CANCELLED ===");
        logger.info("Reservation ID: {} has been cancelled", reservation.getReservationId());
        logger.info("Book ISBN: {}", reservation.getIsbn());
        logger.info("============================\n");
    }
    
    /**
     * Simulate sending email notification
     */
    private void sendEmailNotification(Patron patron, Reservation reservation) {
        logger.debug("Sending email to: {}", patron.getEmail());
        logger.debug("Subject: Your reserved book is ready for pickup");
        logger.debug("Message: Dear {}, your reserved book (ISBN: {}) is now ready for pickup.", 
                patron.getName(), reservation.getIsbn());
    }
}
