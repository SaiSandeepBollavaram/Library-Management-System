package org.librarymanagement.observer;

import org.librarymanagement.mainentities.LendingRecord;

public class EmailNotificationObserver implements LibraryEventObserver {
    @Override
    public void onBookBorrowed(LendingRecord record) {
        sendEmail(record.getPatronId(),
                "Book Borrowed Successfully",
                String.format("You have borrowed book with ISBN: %s. Due date: %s",
                        record.getIsbn(), record.getDueDate()));
    }

    @Override
    public void onBookReturned(LendingRecord record) {
        sendEmail(record.getPatronId(),
                "Book Returned Successfully",
                String.format("You have returned book with ISBN: %s. Thank you!",
                        record.getIsbn()));
    }

    private void sendEmail(String recipient, String subject, String body) {
        // Simulate email sending
        System.out.println("Sending email to: " + recipient);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("---");
    }
}
