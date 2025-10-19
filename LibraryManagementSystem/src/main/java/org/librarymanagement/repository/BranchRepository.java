package org.librarymanagement.repository;

import org.librarymanagement.mainentities.Branch;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Branch entity operations.
 * Defines contract for branch data access.
 */
public interface BranchRepository {
    
    /**
     * Save a branch to the repository
     */
    void save(Branch branch);
    
    /**
     * Find a branch by its ID
     */
    Optional<Branch> findById(String branchId);
    
    /**
     * Get all branches
     */
    List<Branch> findAll();
    
    /**
     * Update an existing branch
     */
    void update(Branch branch);
    
    /**
     * Delete a branch by ID
     */
    void deleteById(String branchId);
    
    /**
     * Check if a branch exists
     */
    boolean existsById(String branchId);
}
