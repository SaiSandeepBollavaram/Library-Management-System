package org.librarymanagement.mainentities;

import java.util.Objects;

/**
 * Represents a library branch in the multi-branch system.
 * Each branch has its own location, contact information, and book inventory.
 */
public class Branch {
    private final String branchId;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    
    public Branch(String branchId, String name, String address, String phoneNumber, String email) {
        this.branchId = branchId;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    public String getBranchId() {
        return branchId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
}
