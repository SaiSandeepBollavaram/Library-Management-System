package org.librarymanagement.mainentities;

/**
 * Enumeration representing the status of a book reservation.
 */
public enum ReservationStatus {

    ACTIVE,          // Reservation is active and patron is waiting
    AVAILABLE,       // Book is available for pickup
    FULFILLED,       // Patron has picked up the book
    CANCELLED,       // Patron cancelled the reservation
    EXPIRED          // Reservation expired (patron didn't pick up in time)
}