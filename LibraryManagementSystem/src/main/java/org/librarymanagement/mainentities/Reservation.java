package org.librarymanagement.mainentities;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a reservation for a book that is currently unavailable.
 * Implements a queue system for managing multiple reservations on the same book.
 */
public class Reservation {
    private final String reservationId;
    private final String isbn;
    private final String patronId;
    private ReservationStatus status;
    private final LocalDateTime reservationDate;
    private LocalDateTime expiryDate;
    private LocalDateTime notificationSentDate;
    private int queuePosition;
    
    public Reservation(String isbn, String patronId) {
        this.reservationId = UUID.randomUUID().toString();
        this.isbn = isbn;
        this.patronId = patronId;
        this.status = ReservationStatus.ACTIVE;
        this.reservationDate = LocalDateTime.now();
        this.queuePosition = 0; // Will be set by the service
    }
    
    public String getReservationId() {
        return reservationId;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public String getPatronId() {
        return patronId;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getReservationDate() {
        return reservationDate;
    }
    
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public LocalDateTime getNotificationSentDate() {
        return notificationSentDate;
    }
    
    public void setNotificationSentDate(LocalDateTime notificationSentDate) {
        this.notificationSentDate = notificationSentDate;
    }
    
    public int getQueuePosition() {
        return queuePosition;
    }
    
    public void setQueuePosition(int queuePosition) {
        this.queuePosition = queuePosition;
    }
    
    public boolean isExpired() {
        return expiryDate != null && LocalDateTime.now().isAfter(expiryDate);
    }
    
}


