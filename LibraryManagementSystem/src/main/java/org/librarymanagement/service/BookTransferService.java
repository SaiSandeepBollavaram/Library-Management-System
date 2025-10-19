package org.librarymanagement.service;

import org.librarymanagement.mainentities.Book;
import org.librarymanagement.mainentities.BookTransferRequest;
import org.librarymanagement.mainentities.TransferStatus;
import org.librarymanagement.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for managing book transfers between library branches.
 * Handles the creation, tracking, and completion of book transfer requests.
 */
public class BookTransferService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookTransferService.class);
    private final BookRepository bookRepository;
    private final BranchService branchService;
    private final Map<String, BookTransferRequest> transferRequests;
    
    public BookTransferService(BookRepository bookRepository, BranchService branchService) {
        this.bookRepository = bookRepository;
        this.branchService = branchService;
        this.transferRequests = new HashMap<>();
    }
    
    /**
     * Initiates a transfer request for a book from one branch to another.
     * 
     * @param isbn The ISBN of the book to transfer
     * @param sourceBranchId The ID of the branch where the book currently is
     * @param destinationBranchId The ID of the branch where the book should be transferred
     * @return The created BookTransferRequest
     * @throws IllegalArgumentException if validation fails
     */
    public BookTransferRequest initiateTransfer(String isbn, String sourceBranchId, String destinationBranchId) {
        // Validate input
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        if (sourceBranchId == null || destinationBranchId == null) {
            throw new IllegalArgumentException("Branch IDs cannot be null");
        }
        if (sourceBranchId.equals(destinationBranchId)) {
            throw new IllegalArgumentException("Source and destination branches must be different");
        }
        
        // Validate that the book exists
        Optional<Book> bookOpt = bookRepository.findByIsbn(isbn);
        if (bookOpt.isEmpty()) {
            logger.error("Book not found for transfer: {}", isbn);
            throw new IllegalArgumentException("Book not found: " + isbn);
        }
        
        Book book = bookOpt.get();
        
        // Validate that the book is at the source branch
        if (!sourceBranchId.equals(book.getBranchId())) {
            logger.error("Book {} is not at source branch {}. Current branch: {}", 
                    isbn, sourceBranchId, book.getBranchId());
            throw new IllegalArgumentException(
                    "Book is not at the source branch. Expected: " + sourceBranchId + 
                    ", Actual: " + book.getBranchId());
        }
        
        // Validate that both branches exist
        if (!branchService.branchExists(sourceBranchId)) {
            logger.error("Source branch not found: {}", sourceBranchId);
            throw new IllegalArgumentException("Source branch not found: " + sourceBranchId);
        }
        if (!branchService.branchExists(destinationBranchId)) {
            logger.error("Destination branch not found: {}", destinationBranchId);
            throw new IllegalArgumentException("Destination branch not found: " + destinationBranchId);
        }
        
        // Create the transfer request
        BookTransferRequest transferRequest = new BookTransferRequest(isbn, sourceBranchId, destinationBranchId);
        transferRequest.setStatus(TransferStatus.PENDING);
        transferRequests.put(transferRequest.getTransferId(), transferRequest);
        
        logger.info("Transfer request initiated: {} for book {} from branch {} to branch {}", 
                transferRequest.getTransferId(), isbn, sourceBranchId, destinationBranchId);
        
        return transferRequest;
    }
    
    /**
     * Completes a transfer request by updating the book's branch location.
     * 
     * @param transferId The ID of the transfer request to complete
     * @throws IllegalArgumentException if the transfer request is not found or already completed
     */
    public void completeTransfer(String transferId) {
        if (transferId == null || transferId.trim().isEmpty()) {
            throw new IllegalArgumentException("Transfer ID cannot be null or empty");
        }
        
        // Find the transfer request
        BookTransferRequest transferRequest = transferRequests.get(transferId);
        if (transferRequest == null) {
            logger.error("Transfer request not found: {}", transferId);
            throw new IllegalArgumentException("Transfer request not found: " + transferId);
        }
        
        // Check if transfer is already completed
        if (transferRequest.getStatus() == TransferStatus.COMPLETED) {
            logger.warn("Transfer request already completed: {}", transferId);
            throw new IllegalArgumentException("Transfer request already completed: " + transferId);
        }
        
        // Check if transfer was cancelled or rejected
        if (transferRequest.getStatus() == TransferStatus.CANCELLED || 
            transferRequest.getStatus() == TransferStatus.REJECTED) {
            logger.error("Cannot complete a cancelled or rejected transfer: {}", transferId);
            throw new IllegalArgumentException("Cannot complete a cancelled or rejected transfer: " + transferId);
        }
        
        // Get the book and update its branch
        String isbn = transferRequest.getIsbn();
        Optional<Book> bookOpt = bookRepository.findByIsbn(isbn);
        
        if (bookOpt.isEmpty()) {
            logger.error("Book not found for transfer completion: {}", isbn);
            throw new IllegalArgumentException("Book not found: " + isbn);
        }
        
        Book book = bookOpt.get();
        String oldBranchId = book.getBranchId();
        book.setBranchId(transferRequest.getDestinationBranchId());
        bookRepository.modify(book);
        
        // Update transfer request status
        transferRequest.setStatus(TransferStatus.COMPLETED);
        transferRequest.setRemarks("Transfer completed successfully");
        
        logger.info("Transfer completed: {} - Book {} moved from branch {} to branch {}", 
                transferId, isbn, oldBranchId, transferRequest.getDestinationBranchId());
    }
    
    /**
     * Cancels a pending transfer request.
     * 
     * @param transferId The ID of the transfer request to cancel
     * @throws IllegalArgumentException if the transfer request is not found or not pending
     */
    public void cancelTransfer(String transferId) {
        if (transferId == null || transferId.trim().isEmpty()) {
            throw new IllegalArgumentException("Transfer ID cannot be null or empty");
        }
        
        BookTransferRequest transferRequest = transferRequests.get(transferId);
        if (transferRequest == null) {
            logger.error("Transfer request not found: {}", transferId);
            throw new IllegalArgumentException("Transfer request not found: " + transferId);
        }
        
        if (transferRequest.getStatus() != TransferStatus.PENDING) {
            logger.error("Cannot cancel transfer in status: {}", transferRequest.getStatus());
            throw new IllegalArgumentException("Can only cancel pending transfers");
        }
        
        transferRequest.setStatus(TransferStatus.CANCELLED);
        transferRequest.setRemarks("Transfer cancelled by user");
        
        logger.info("Transfer cancelled: {}", transferId);
    }
    
    /**
     * Gets a transfer request by its ID.
     * 
     * @param transferId The ID of the transfer request
     * @return Optional containing the transfer request if found
     */
    public Optional<BookTransferRequest> getTransferRequest(String transferId) {
        return Optional.ofNullable(transferRequests.get(transferId));
    }
}
