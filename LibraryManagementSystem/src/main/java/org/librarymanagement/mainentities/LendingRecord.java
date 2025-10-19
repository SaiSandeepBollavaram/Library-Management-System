package org.librarymanagement.mainentities;

import java.time.LocalDateTime;

public class LendingRecord {
    private final String recordId;
    private final String patronId;
    private final String isbn;
    private final LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private LocalDateTime dueDate;


    public LendingRecord(String recordId, String patronId, String isbn, LocalDateTime borrowDate,  LocalDateTime dueDate) {
        this.recordId = recordId;
        this.patronId = patronId;
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getPatronId() {
        return patronId;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public boolean isActive() {
        return returnDate == null;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isOverdue() {
        return returnDate != null && LocalDateTime.now().isAfter(dueDate);
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

}
