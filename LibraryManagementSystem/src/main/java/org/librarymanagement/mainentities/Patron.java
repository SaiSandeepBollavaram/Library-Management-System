package org.librarymanagement.mainentities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Patron {
    private final String patronId;
    private String name;
    private String email;
    private String phoneNumber;
    private PatronType patronType;
    private final List<LendingRecord> borrowingHistory;

    public Patron(String patronId, String name, String phoneNumber, String email, PatronType patronType) {
        this.patronId = patronId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.patronType = patronType;
        this.borrowingHistory = new ArrayList<>();
    }

    public String getPatronId() {
        return patronId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PatronType getPatronType() {
        return patronType;
    }

    public void setPatronType(PatronType patronType) {
        this.patronType = patronType;
    }

    public List<LendingRecord> getBorrowingHistory() {
        return Collections.unmodifiableList(borrowingHistory);
    }

    public String getEmail() {
        return email;
    }

    public int getBorrowLimit() {
        return patronType.getMaxBorrowLimit();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void addLendingRecord(LendingRecord record){
        this.borrowingHistory.add(record);
    }

    public long getBorrowingLimit(){
        return patronType.getMaxBorrowLimit();
    }

    public long getCurrentBorrowedCount(){
        return borrowingHistory.stream().filter(record -> record.getReturnDate() == null).count();
    }

    public boolean canBorrowMore(){
        return getCurrentBorrowedCount() < getBorrowingLimit();
    }
}
