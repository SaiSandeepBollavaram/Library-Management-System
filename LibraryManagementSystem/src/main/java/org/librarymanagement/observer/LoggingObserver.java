package org.librarymanagement.observer;


import org.librarymanagement.mainentities.LendingRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingObserver implements LibraryEventObserver {
    private static final Logger logger = LoggerFactory.getLogger(LoggingObserver.class);

    @Override
    public void onBookBorrowed(LendingRecord record) {
        logger.info("Book borrowed - ISBN: {}, Patron: {}, Due Date: {}",
                record.getIsbn(), record.getPatronId(), record.getDueDate());
    }

    @Override
    public void onBookReturned(LendingRecord record) {
        logger.info("Book returned - ISBN: {}, Patron: {}, Return Date: {}",
                record.getIsbn(), record.getPatronId(), record.getReturnDate());
    }
}
