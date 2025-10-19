package org.librarymanagement.observer;

/**
 * Observer interface for reservation-specific events.
 * Implements Observer Pattern for event-driven notifications related to reservations.
 */
public interface ReservationObserver {
    /**
     * Update method called when a reservation event occurs
     * 
     * @param event The type of event (e.g., "RESERVATION_CREATED", "RESERVATION_READY", "RESERVATION_CANCELLED")
     * @param data The data associated with the event
     */
    void update(String event, Object data);
}
