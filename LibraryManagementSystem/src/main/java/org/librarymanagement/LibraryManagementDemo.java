package org.librarymanagement;

import org.librarymanagement.mainentities.*;
import org.librarymanagement.observer.EmailNotificationObserver;
import org.librarymanagement.observer.LoggingObserver;
import org.librarymanagement.observer.ReservationNotificationObserver;
import org.librarymanagement.repository.*;
import org.librarymanagement.service.*;
import org.librarymanagement.strategy.*;

import java.util.List;

/**
 * Comprehensive demonstration of the Library Management System.
 * 
 * This demo showcases both CORE REQUIREMENTS and ADVANCED FEATURES:
 * 
 * PART A - CORE REQUIREMENTS:
 * 1. Book Management - Add, search, and manage books
 * 2. Patron Management - Create patrons using Factory Pattern
 * 3. Book Search - Strategy Pattern (Title, Author, ISBN)
 * 4. Borrowing System - Checkout books with lending records
 * 5. Return System - Return books and update inventory
 * 6. Inventory Management - View available books
 * 7. Patron Status - Check borrowing limits and active loans
 * 
 * PART B - ADVANCED FEATURES:
 * 1. Multi-Branch System - Multiple library branches
 * 2. Book Transfers - Transfer books between branches
 * 3. Reservation System - Queue management for popular books
 * 4. Observer Pattern - Real-time notifications for events
 * 5. Recommendation Engine - Personalized book recommendations
 * 6. Multiple Search Strategies - Flexible search capabilities
 */
public class LibraryManagementDemo {
    
    // Core services
    private final BookService bookService;
    private final PatronService patronService;
    private final LendingService lendingService;
    
    // Advanced services
    private final BranchService branchService;
    private final BookTransferService transferService;
    private final ReservationService reservationService;
    private final RecommendationService recommendationService;
    
    public LibraryManagementDemo(BookService bookService, PatronService patronService, 
                                 LendingService lendingService, BranchService branchService,
                                 BookTransferService transferService, ReservationService reservationService,
                                 RecommendationService recommendationService) {
        this.bookService = bookService;
        this.patronService = patronService;
        this.lendingService = lendingService;
        this.branchService = branchService;
        this.transferService = transferService;
        this.reservationService = reservationService;
        this.recommendationService = recommendationService;
    }
    
    public static void main(String[] args) {
        printHeader();
        
        // Initialize the application
        LibraryManagementDemo demo = initializeApplication();
        
        // Run all demonstrations
        demo.runFullDemo();
        
        printFooter();
    }
    
    private static void printHeader() {
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                  ║");
        System.out.println("║              LIBRARY MANAGEMENT SYSTEM - DEMO                    ║");
        System.out.println("║                                                                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private static void printFooter() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                  ║");
        System.out.println("║           ✅ ALL DEMONSTRATIONS COMPLETED SUCCESSFULLY!          ║");
        System.out.println("║                                                                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Initialize the application with all repositories and services
     */
    private static LibraryManagementDemo initializeApplication() {
        // Initialize repositories
        BookRepository bookRepository = new InMemoryBookRepository();
        PatronRepository patronRepository = new InMemoryPatronRepository();
        LendingRepository lendingRepository = new InMemoryLendingRepository();
        BranchRepository branchRepository = new InMemoryBranchRepository();
        ReservationRepository reservationRepository = new InMemoryReservationRepository();
        
        // Initialize core services
        BookService bookService = new BookService(bookRepository);
        PatronService patronService = new PatronService(patronRepository);
        LendingService lendingService = new LendingService(bookRepository, patronRepository, lendingRepository);
        
        // Initialize advanced services
        BranchService branchService = new BranchService(branchRepository);
        BookTransferService transferService = new BookTransferService(bookRepository, branchService);
        ReservationService reservationService = new ReservationService(
                reservationRepository, bookRepository, patronRepository);
        RecommendationService recommendationService = new RecommendationService(
                bookRepository, patronRepository, new AuthorBasedRecommendationStrategy());
        
        // Integrate services
        lendingService.setReservationService(reservationService);
        
        // Setup observers
        setupObservers(lendingService, reservationService);
        
        return new LibraryManagementDemo(bookService, patronService, lendingService,
                branchService, transferService, reservationService, recommendationService);
    }
    
    /**
     * Setup observers for event notifications
     */
    private static void setupObservers(LendingService lendingService, ReservationService reservationService) {
        // Add observers to lending service
        lendingService.addObserver(new LoggingObserver());
        lendingService.addObserver(new EmailNotificationObserver());
        
        // Add observers to reservation service
        reservationService.addObserver(new ReservationNotificationObserver());
    }
    
    /**
     * Run the complete demonstration
     */
    private void runFullDemo() {
        // ===== PART A: CORE REQUIREMENTS =====
        printSectionHeader("PART A: CORE REQUIREMENTS");
        
        // Setup data for core features
        List<Book> coreBooks = demonstrateCoreBookManagement();
        List<Patron> corePatrons = demonstrateCorePatronManagement();
        
        // Core features
        demonstrateCoreBookSearch();
        demonstrateCoreBorrowingSystem(coreBooks.get(0), corePatrons.get(0));
        demonstrateCoreInventoryManagement();
        demonstrateCoreReturnSystem(coreBooks.get(0), corePatrons.get(0));
        demonstrateCorePatronStatus(corePatrons.get(0));
        demonstrateCoreMultipleBorrows(coreBooks.get(1), corePatrons.get(0));
        demonstrateCoreActiveLendings(corePatrons.get(0));
        demonstrateCoreUpdateBook(coreBooks.get(2));
        demonstrateCoreRemoveBook();
        demonstrateCoreUpdatePatron(corePatrons.get(1));
        
        // ===== PART B: ADVANCED FEATURES =====
        printSectionHeader("PART B: ADVANCED FEATURES");
        
        // Setup data for advanced features
        setupBranches();
        List<Book> advancedBooks = setupAdvancedBooks();
        List<Patron> advancedPatrons = setupAdvancedPatrons();
        
        // Advanced features
        demonstrateBookTransfer(advancedBooks);
        demonstrateReservationSystem(advancedPatrons, advancedBooks);
        demonstrateRecommendationEngine(advancedPatrons);
    }
    
    private void printSectionHeader(String title) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  " + title);
        System.out.println("=".repeat(70) + "\n");
    }
    
    private void printFeatureHeader(String featureNumber, String featureName) {
        System.out.println("\n" + "─".repeat(70));
        System.out.println("📌 " + featureNumber + ". " + featureName);
        System.out.println("─".repeat(70));
    }
    
    // ==================== CORE REQUIREMENTS ====================
    
    /**
     * CORE-1: Book Management - Add books to the library
     */
    private List<Book> demonstrateCoreBookManagement() {
        printFeatureHeader("CORE-1", "BOOK MANAGEMENT");
        System.out.println("Adding books to the library...\n");
        
        Book book1 = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2017);
        Book book2 = new Book("978-0596009205", "Head First Design Patterns", "Eric Freeman", 2004);
        Book book3 = new Book("978-0132350884", "Clean Code", "Robert Martin", 2008);
        
        bookService.addBook(book1);
        bookService.addBook(book2);
        bookService.addBook(book3);
        
        System.out.println("✓ Added 3 books to the library:");
        System.out.println("  1. " + book1.getTitle() + " by " + book1.getAuthor());
        System.out.println("  2. " + book2.getTitle() + " by " + book2.getAuthor());
        System.out.println("  3. " + book3.getTitle() + " by " + book3.getAuthor());
        
        return List.of(book1, book2, book3);
    }
    
    /**
     * CORE-2: Patron Management - Create patrons using Factory Pattern
     */
    private List<Patron> demonstrateCorePatronManagement() {
        printFeatureHeader("CORE-2", "PATRON MANAGEMENT (Factory Pattern)");
        System.out.println("Creating patrons using Factory Pattern...\n");
        
        Patron studentJohn = PatronFactory.createStudent("John Doe", "john@example.com", "555-0001");
        Patron facultySmith = PatronFactory.createFaculty("Dr. Smith", "smith@example.com", "555-0002");
        
        patronService.addPatron(studentJohn);
        patronService.addPatron(facultySmith);
        
        System.out.println("✓ Created 2 patrons:");
        System.out.println("  1. " + studentJohn.getName() + " (Student, ID: " + studentJohn.getPatronId() + 
                ", Limit: " + studentJohn.getBorrowLimit() + " books)");
        System.out.println("  2. " + facultySmith.getName() + " (Faculty, ID: " + facultySmith.getPatronId() + 
                ", Limit: " + facultySmith.getBorrowLimit() + " books)");
        
        return List.of(studentJohn, facultySmith);
    }
    
    /**
     * CORE-3: Book Search - Strategy Pattern implementation
     */
    private void demonstrateCoreBookSearch() {
        printFeatureHeader("CORE-3", "BOOK SEARCH (Strategy Pattern)");
        System.out.println("Searching books using different strategies...\n");
        
        // Search by title
        System.out.println("🔍 Search by Title: 'Java'");
        List<Book> titleResults = bookService.searchBooks(new TitleSearchStrategy(), "Java");
        displaySearchResults(titleResults);
        
        // Search by author
        System.out.println("🔍 Search by Author: 'Martin'");
        List<Book> authorResults = bookService.searchBooks(new AuthorSearchStrategy(), "Martin");
        displaySearchResults(authorResults);
        
        // Search by ISBN
        System.out.println("🔍 Search by ISBN: '978-0134685991'");
        List<Book> isbnResults = bookService.searchBooks(new ISBNSearchStrategy(), "978-0134685991");
        displaySearchResults(isbnResults);
    }
    
    private void displaySearchResults(List<Book> results) {
        System.out.println("  Found " + results.size() + " result(s):");
        results.forEach(book -> System.out.println("    • " + book.getTitle() + " by " + book.getAuthor()));
        System.out.println();
    }
    
    /**
     * CORE-4: Borrowing System
     */
    private void demonstrateCoreBorrowingSystem(Book book, Patron patron) {
        printFeatureHeader("CORE-4", "BORROWING SYSTEM");
        System.out.println("Patron: " + patron.getName() + " is borrowing: " + book.getTitle() + "\n");
        
        LendingRecord record = lendingService.borrowBook(book.getIsbn(), patron.getPatronId());
        
        System.out.println("✓ Book borrowed successfully!");
        System.out.println("  Lending Record ID: " + record.getRecordId());
        System.out.println("  Due Date: " + record.getDueDate());
    }
    
    /**
     * CORE-5: Inventory Management
     */
    private void demonstrateCoreInventoryManagement() {
        printFeatureHeader("CORE-5", "INVENTORY MANAGEMENT");
        System.out.println("Checking available books in inventory...\n");
        
        List<Book> availableBooks = bookService.getAvailableBooks();
        System.out.println("✓ Available books: " + availableBooks.size());
        availableBooks.forEach(book -> 
                System.out.println("  • " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")"));
    }
    
    /**
     * CORE-6: Return System
     */
    private void demonstrateCoreReturnSystem(Book book, Patron patron) {
        printFeatureHeader("CORE-6", "RETURN SYSTEM");
        System.out.println("Patron: " + patron.getName() + " is returning: " + book.getTitle() + "\n");
        
        lendingService.returnBook(book.getIsbn(), patron.getPatronId());
        
        System.out.println("✓ Book returned successfully!");
    }
    
    /**
     * CORE-7: Patron Status
     */
    private void demonstrateCorePatronStatus(Patron patron) {
        printFeatureHeader("CORE-7", "PATRON STATUS");
        System.out.println("Checking patron status...\n");
        
        System.out.println("✓ Patron Information:");
        System.out.println("  Name: " + patron.getName());
        System.out.println("  Type: " + patron.getPatronType());
        System.out.println("  Can borrow more: " + (patron.canBorrowMore() ? "Yes" : "No"));
        System.out.println("  Current borrowed: " + patron.getCurrentBorrowedCount() + " / " + patron.getBorrowLimit());
    }
    
    /**
     * CORE-8: Multiple Borrows
     */
    private void demonstrateCoreMultipleBorrows(Book book, Patron patron) {
        printFeatureHeader("CORE-8", "MULTIPLE BORROWS");
        System.out.println("Patron: " + patron.getName() + " is borrowing another book: " + book.getTitle() + "\n");
        
        lendingService.borrowBook(book.getIsbn(), patron.getPatronId());
        
        System.out.println("✓ Second book borrowed successfully!");
        System.out.println("  Total books borrowed: " + patron.getCurrentBorrowedCount());
    }
    
    /**
     * CORE-9: Active Lendings
     */
    private void demonstrateCoreActiveLendings(Patron patron) {
        printFeatureHeader("CORE-9", "ACTIVE LENDINGS");
        System.out.println("Viewing active lendings for: " + patron.getName() + "\n");
        
        List<LendingRecord> activeBorrows = lendingService.getPatronActiveBorrows(patron.getPatronId());
        System.out.println("✓ Active lendings: " + activeBorrows.size());
        
        activeBorrows.forEach(record -> {
            bookService.findByIsbn(record.getIsbn()).ifPresent(book -> 
                System.out.println("  • " + book.getTitle() + " (Due: " + record.getDueDate() + ")"));
        });
    }
    
    /**
     * CORE-10: Update Book Information
     */
    private void demonstrateCoreUpdateBook(Book book) {
        printFeatureHeader("CORE-10", "UPDATE BOOK INFORMATION");
        System.out.println("Updating book information...\n");
        
        System.out.println("Original Information:");
        System.out.println("  Title: " + book.getTitle());
        System.out.println("  Author: " + book.getAuthor());
        System.out.println("  Year: " + book.getPublicationYear() + "\n");
        
        // Update book details
        book.setTitle(book.getTitle() + " (3rd Edition)");
        book.setPublicationYear(2023);
        bookService.modifyBook(book);
        
        System.out.println("✓ Book updated successfully!");
        System.out.println("\nUpdated Information:");
        System.out.println("  Title: " + book.getTitle());
        System.out.println("  Author: " + book.getAuthor());
        System.out.println("  Year: " + book.getPublicationYear());
    }
    
    /**
     * CORE-11: Remove Book
     */
    private void demonstrateCoreRemoveBook() {
        printFeatureHeader("CORE-11", "REMOVE BOOK");
        System.out.println("Removing a book from the library...\n");
        
        // Add a book specifically to remove it
        Book bookToRemove = new Book("978-9999999999", "Temporary Book", "Test Author", 2020);
        bookService.addBook(bookToRemove);
        System.out.println("Added temporary book: " + bookToRemove.getTitle());
        
        // Now remove it
        boolean removed = bookService.removeBook(bookToRemove.getIsbn());
        
        if (removed) {
            System.out.println("\n✓ Book removed successfully!");
            System.out.println("  ISBN: " + bookToRemove.getIsbn());
            System.out.println("  Title: " + bookToRemove.getTitle());
        } else {
            System.out.println("\n✗ Failed to remove book");
        }
    }
    
    /**
     * CORE-12: Update Patron Information
     */
    private void demonstrateCoreUpdatePatron(Patron patron) {
        printFeatureHeader("CORE-12", "UPDATE PATRON INFORMATION");
        System.out.println("Updating patron information...\n");
        
        System.out.println("Original Information:");
        System.out.println("  Name: " + patron.getName());
        System.out.println("  Email: " + patron.getEmail());
        System.out.println("  Phone: " + patron.getPhoneNumber() + "\n");
        
        // Update patron details
        patron.setEmail("updated." + patron.getEmail());
        patron.setPhoneNumber("555-9999");
        patronService.updatePatron(patron);
        
        System.out.println("✓ Patron updated successfully!");
        System.out.println("\nUpdated Information:");
        System.out.println("  Name: " + patron.getName());
        System.out.println("  Email: " + patron.getEmail());
        System.out.println("  Phone: " + patron.getPhoneNumber());
    }
    
    // ==================== ADVANCED FEATURES ====================
    
    /**
     * Setup branches for advanced features
     */
    private void setupBranches() {
        printFeatureHeader("ADVANCED-SETUP", "MULTI-BRANCH SYSTEM SETUP");
        System.out.println("Setting up library branches...\n");
        
        Branch downtown = new Branch("BR001", "Downtown Library", 
                "123 Main St, Downtown", "555-0101", "downtown@library.com");
        Branch suburban = new Branch("BR002", "Suburban Library", 
                "456 Oak Ave, Suburb", "555-0102", "suburban@library.com");
        Branch university = new Branch("BR003", "University Library", 
                "789 Campus Dr, University", "555-0103", "university@library.com");
        
        branchService.registerBranch(downtown);
        branchService.registerBranch(suburban);
        branchService.registerBranch(university);
        
        System.out.println("✓ Created 3 library branches:");
        branchService.getAllBranches().forEach(b -> 
                System.out.println("  • " + b.getName() + " (" + b.getBranchId() + ")"));
    }
    
    /**
     * Setup books with branch locations
     */
    private List<Book> setupAdvancedBooks() {
        System.out.println("\n📚 Adding books to branches...\n");
        
        Book book1 = new Book("978-0201633610", "Design Patterns", "Gang of Four", 1994, "BR001");
        Book book2 = new Book("978-0134494166", "Clean Architecture", "Robert Martin", 2017, "BR002");
        Book book3 = new Book("978-0137081073", "The Clean Coder", "Robert Martin", 2011, "BR003");
        
        bookService.addBook(book1);
        bookService.addBook(book2);
        bookService.addBook(book3);
        
        System.out.println("✓ Added books to branches:");
        System.out.println("  • Downtown (BR001): " + book1.getTitle());
        System.out.println("  • Suburban (BR002): " + book2.getTitle());
        System.out.println("  • University (BR003): " + book3.getTitle());
        
        return List.of(book1, book2, book3);
    }

    /**
     * Setup patrons for advanced features
     */
    private List<Patron> setupAdvancedPatrons() {
        System.out.println("\n Creating patrons for advanced features...\n");

        Patron alice = PatronFactory.createStudent("Alice Johnson", "alice@email.com", "555-2001");
        Patron bob = PatronFactory.createFaculty("Dr. Bob Wilson", "bob@email.com", "555-2002");
        Patron carol = PatronFactory.createStudent("Carol Brown", "carol@email.com", "555-2003");

        patronService.addPatron(alice);
        patronService.addPatron(bob);
        patronService.addPatron(carol);

        System.out.println("✓ Created 3 patrons:");
        System.out.println("  • " + alice.getName() + " (" + alice.getPatronId() + ")");
        System.out.println("  • " + bob.getName() + " (" + bob.getPatronId() + ")");
        System.out.println("  • " + carol.getName() + " (" + carol.getPatronId() + ")");

        return List.of(alice, bob, carol);
    }
    
    /**
     * ADVANCED-1: Book Transfer Between Branches
     */
    private void demonstrateBookTransfer(List<Book> books) {
        printFeatureHeader("ADVANCED-1", "BOOK TRANSFER BETWEEN BRANCHES");
        System.out.println("Transferring book between branches...\n");
        
        Book book = books.get(1); // Clean Architecture
        String isbn = book.getIsbn();
        
        System.out.println("Scenario: Transferring '" + book.getTitle() + "' from Suburban (BR002) to Downtown (BR001)");
        System.out.println("  Before: Book is at branch " + book.getBranchId() + "\n");
        
        BookTransferRequest transferRequest = transferService.initiateTransfer(isbn, "BR002", "BR001");
        System.out.println("✓ Transfer request created:");
        System.out.println("  Transfer ID: " + transferRequest.getTransferId());
        System.out.println("  Status: " + transferRequest.getStatus() + "\n");
        
        transferService.completeTransfer(transferRequest.getTransferId());
        
        Book updatedBook = bookService.findByIsbn(isbn).orElseThrow();
        System.out.println("✓ Transfer completed successfully!");
        System.out.println("  After: Book is now at branch " + updatedBook.getBranchId());
    }
    
    /**
     * ADVANCED-2: Reservation System with Queue Management
     */
    private void demonstrateReservationSystem(List<Patron> patrons, List<Book> books) {
        printFeatureHeader("ADVANCED-2", "RESERVATION SYSTEM (Queue Management)");
        System.out.println("Demonstrating reservation queue for popular books...\n");
        
        Patron alice = patrons.get(0);
        Patron bob = patrons.get(1);
        Patron carol = patrons.get(2);
        
        Book popularBook = books.get(0);
        String isbn = popularBook.getIsbn();
        
        System.out.println("Scenario: Multiple patrons want the same book: " + popularBook.getTitle());
        System.out.println();
        
        // Alice borrows the book first
        System.out.println("Step 1: Alice borrows the book");
        lendingService.borrowBook(isbn, alice.getPatronId());
        System.out.println("  ✓ Book borrowed by Alice\n");
        
        // Bob tries to borrow but reserves instead
        System.out.println("Step 2: Bob wants the book but it's checked out, so he reserves it");
        Reservation bobReservation = reservationService.createReservation(isbn, bob.getPatronId());
        System.out.println("  ✓ Bob's reservation created (Queue position: " + bobReservation.getQueuePosition() + ")\n");
        
        // Carol also reserves
        System.out.println("Step 3: Carol also reserves the same book");
        Reservation carolReservation = reservationService.createReservation(isbn, carol.getPatronId());
        System.out.println("  ✓ Carol's reservation created (Queue position: " + carolReservation.getQueuePosition() + ")\n");
        
        // Alice returns the book - Bob should be notified
        System.out.println("Step 4: Alice returns the book");
        lendingService.returnBook(isbn, alice.getPatronId());
        System.out.println("  ✓ Book returned");
        System.out.println("  ✓ Bob (next in queue) has been notified that the book is ready!");
    }
    
    /**
     * ADVANCED-3: Book Recommendation Engine
     */
    private void demonstrateRecommendationEngine(List<Patron> patrons) {
        printFeatureHeader("ADVANCED-3", "BOOK RECOMMENDATION ENGINE");
        System.out.println("Generating personalized book recommendations...\n");
        
        Patron alice = patrons.get(0);
        
        System.out.println("Generating recommendations for: " + alice.getName());
        System.out.println("(Based on borrowing history and preferences)\n");
        
        // Author-based recommendations
        System.out.println("🎯 Strategy 1: Author-Based Recommendations");
        recommendationService.setStrategy(new AuthorBasedRecommendationStrategy());
        List<Book> authorRecommendations = recommendationService.getRecommendations(alice.getPatronId(), 3);
        displayRecommendations(authorRecommendations);
        
        // Popularity-based recommendations
        System.out.println("🎯 Strategy 2: Popularity-Based Recommendations");
        PopularityBasedRecommendationStrategy popularityStrategy = new PopularityBasedRecommendationStrategy();
        popularityStrategy.updatePopularityData(patronService.getAllPatrons());
        recommendationService.setStrategy(popularityStrategy);
        List<Book> popularRecommendations = recommendationService.getRecommendations(alice.getPatronId(), 3);
        displayRecommendations(popularRecommendations);
    }
    
    private void displayRecommendations(List<Book> recommendations) {
        if (recommendations.isEmpty()) {
            System.out.println("  No recommendations available at this time.\n");
        } else {
            recommendations.forEach(book -> 
                System.out.println("  • " + book.getTitle() + " by " + book.getAuthor() + 
                        " (" + book.getPublicationYear() + ")"));
            System.out.println();
        }
    }
}
