package org.librarymanagement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.librarymanagement.mainentities.Book;
import org.librarymanagement.mainentities.BookStatus;
import org.librarymanagement.mainentities.LendingRecord;
import org.librarymanagement.mainentities.Patron;
import org.librarymanagement.observer.LibraryEventObserver;
import org.librarymanagement.repository.BookRepository;
import org.librarymanagement.repository.LendingRepository;
import org.librarymanagement.repository.PatronRepository;
import org.librarymanagement.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LendingService {
    private static final Logger logger = LoggerFactory.getLogger(LendingService.class);
    private static final int DEFAULT_LENDING_DAYS = 14;

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final LendingRepository lendingRepository;
    private final List<LibraryEventObserver> observers;
    private ReservationService reservationService; // Optional - for reservation integration

    public LendingService(BookRepository bookRepository,
                          PatronRepository patronRepository,
                          LendingRepository lendingRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.lendingRepository = lendingRepository;
        this.observers = new ArrayList<>();
    }
    
    /**
     * Set the reservation service for integration
     * This allows the lending service to check for reservations when books are returned
     */
    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
        logger.info("ReservationService integrated with LendingService");
    }

  // Observer pattern methods
    public void addObserver(LibraryEventObserver observer) {
      observers.add(observer);
       logger.info("Observer added: {}", observer.getClass().getSimpleName());
    }

   public void removeObserver(LibraryEventObserver observer) {
       observers.remove(observer);
       logger.info("Observer removed: {}", observer.getClass().getSimpleName());
   }

    private void notifyBookBorrowed(LendingRecord record) {
       for (LibraryEventObserver observer : observers) {
            observer.onBookBorrowed(record);
        }
    }

    private void notifyBookReturned(LendingRecord record) {
        for (LibraryEventObserver observer : observers) {
            observer.onBookReturned(record);
        }
    }

    public LendingRecord borrowBook(String isbn, String patronId) {
        // Validate book exists and is available
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + isbn));

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is not available for borrowing: " + isbn);
        }

        // Validate patron exists and can borrow
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new IllegalArgumentException("Patron not found: " + patronId));

        if (!patron.canBorrowMore()) {
            throw new IllegalStateException("Patron has reached borrowing limit: " + patronId);
        }

        // Create lending record
        String recordId = UUID.randomUUID().toString();
        LocalDateTime borrowDate = LocalDateTime.now();
        LocalDateTime dueDate = borrowDate.plusDays(DEFAULT_LENDING_DAYS);

        LendingRecord record = new LendingRecord(recordId, patronId, isbn, borrowDate, dueDate);

        // Update book status
        book.setStatus(BookStatus.BORROWED);
        bookRepository.modify(book);

        // Save lending record
        lendingRepository.add(record);

        // Update patron history
        patron.addLendingRecord(record);
        patronRepository.modify(patron);

        // Notify observers
        notifyBookBorrowed(record);

        logger.info("Book borrowed - ISBN: {}, Patron: {}", isbn, patronId);
        return record;
    }

    public void returnBook(String isbn, String patronId) {
        // Find active lending record
        List<LendingRecord> records = lendingRepository.findByIsbn(isbn);

        LendingRecord activeRecord = records.stream()
                .filter(r -> r.getPatronId().equals(patronId) && r.isActive())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No active lending record found for book: " + isbn + " and patron: " + patronId));

        // Update return date
        activeRecord.setReturnDate(LocalDateTime.now());
        lendingRepository.modify(activeRecord);

        // Update book status
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + isbn));
        book.setStatus(BookStatus.AVAILABLE);
        bookRepository.modify(book);

        // Notify observers
        notifyBookReturned(activeRecord);

        logger.info("Book returned - ISBN: {}, Patron: {}", isbn, patronId);
        
        // Check for reservations and notify next patron in queue
        if (reservationService != null) {
            try {
                reservationService.processBookReturn(isbn);
                logger.info("Processed reservations for returned book: {}", isbn);
            } catch (Exception e) {
                logger.error("Error processing reservations for book: " + isbn, e);
            }
        }
    }

    public List<LendingRecord> getPatronActiveBorrows(String patronId) {
        // find active borrows of patron
        return lendingRepository.findActiveLendings(patronId);
    }

}
