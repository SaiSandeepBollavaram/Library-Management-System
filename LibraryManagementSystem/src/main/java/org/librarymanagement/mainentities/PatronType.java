package org.librarymanagement.mainentities;

public enum PatronType {
    STUDENT(5),
    FACULTY(10);

    private final int maxBorrowLimit;

    PatronType(int maxBorrowLimit) {
        this.maxBorrowLimit = maxBorrowLimit;
    }

    public int getMaxBorrowLimit() {
        return maxBorrowLimit;
    }

}
