package org.librarymanagement.observer;

import org.librarymanagement.mainentities.LendingRecord;

public interface LibraryEventObserver {
    void onBookBorrowed(LendingRecord record);
    void onBookReturned(LendingRecord record);
}
