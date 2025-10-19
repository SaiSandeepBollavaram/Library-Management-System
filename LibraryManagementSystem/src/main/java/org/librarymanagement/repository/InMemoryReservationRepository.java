package org.librarymanagement.repository;

import org.librarymanagement.mainentities.Reservation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of ReservationRepository.
 * Thread-safe using ConcurrentHashMap.
 */
public class InMemoryReservationRepository implements ReservationRepository {
    
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();
    
    @Override
    public void save(Reservation reservation) {
        if (reservation == null || reservation.getReservationId() == null) {
            throw new IllegalArgumentException("Reservation and reservationId cannot be null");
        }
        reservations.put(reservation.getReservationId(), reservation);
    }
    
    @Override
    public Optional<Reservation> findById(String reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }
    
    @Override
    public List<Reservation> findActiveReservationsByIsbn(String isbn) {
        return reservations.values().stream()
                .filter(r -> r.getIsbn().equals(isbn))
                .filter(r -> r.getStatus().name().equals("ACTIVE"))
                .sorted(Comparator.comparingInt(Reservation::getQueuePosition))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findByPatronId(String patronId) {
        return reservations.values().stream()
                .filter(r -> r.getPatronId().equals(patronId))
                .collect(Collectors.toList());
    }
    
    @Override
    public void update(Reservation reservation) {
        if (reservation == null || reservation.getReservationId() == null) {
            throw new IllegalArgumentException("Reservation and reservationId cannot be null");
        }
        if (!reservations.containsKey(reservation.getReservationId())) {
            throw new IllegalArgumentException("Reservation not found: " + reservation.getReservationId());
        }
        reservations.put(reservation.getReservationId(), reservation);
    }
    
    @Override
    public void deleteById(String reservationId) {
        reservations.remove(reservationId);
    }
    
    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }
}
