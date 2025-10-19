package org.librarymanagement.mainentities;

public class Book {
    private final String  isbn;
    private String title;
    private String author;
    private int publicationYear;
    private BookStatus status;
    private String branchId; // Branch where the book is currently located

    public Book(String isbn, String title, String author, int publicationYear) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.status = BookStatus.AVAILABLE;
        this.branchId = null; // Can be set later
    }
    
    public Book(String isbn, String title, String author, int publicationYear, String branchId) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.status = BookStatus.AVAILABLE;
        this.branchId = branchId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public BookStatus getStatus() {
        return status;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
    
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }
}
