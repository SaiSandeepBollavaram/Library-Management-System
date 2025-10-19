package org.librarymanagement.mainentities;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a request to transfer a book from one branch to another.
 * Tracks the transfer status and maintains an audit trail.
 */
public class BookTransferRequest {
    private final String transferId;
    private final String isbn;
    private final String sourceBranchId;
    private final String destinationBranchId;
    private TransferStatus status;
    private final LocalDateTime requestDate;
    private LocalDateTime completionDate;
    private String remarks;
    
    public BookTransferRequest(String isbn, String sourceBranchId, String destinationBranchId) {
        this.transferId = UUID.randomUUID().toString();
        this.isbn = isbn;
        this.sourceBranchId = sourceBranchId;
        this.destinationBranchId = destinationBranchId;
        this.status = TransferStatus.PENDING;
        this.requestDate = LocalDateTime.now();
    }
    
    public String getTransferId() {
        return transferId;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public String getSourceBranchId() {
        return sourceBranchId;
    }
    
    public String getDestinationBranchId() {
        return destinationBranchId;
    }
    
    public TransferStatus getStatus() {
        return status;
    }
    
    public void setStatus(TransferStatus status) {
        this.status = status;
        if (status == TransferStatus.COMPLETED || status == TransferStatus.REJECTED) {
            this.completionDate = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getRequestDate() {
        return requestDate;
    }
    
    public LocalDateTime getCompletionDate() {
        return completionDate;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
}
