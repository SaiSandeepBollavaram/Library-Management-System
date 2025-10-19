package org.librarymanagement.service;

import org.librarymanagement.mainentities.Branch;
import org.librarymanagement.repository.BranchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing library branches.
 * Follows Single Responsibility Principle by handling only branch-related operations.
 */
public class BranchService {
    
    private static final Logger logger = LoggerFactory.getLogger(BranchService.class);
    private final BranchRepository branchRepository;
    
    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }
    
    /**
     * Register a new branch in the system
     */
    public void registerBranch(Branch branch) {
        if (branch == null) {
            throw new IllegalArgumentException("Branch cannot be null");
        }
        
        if (branchRepository.existsById(branch.getBranchId())) {
            logger.warn("Attempted to register duplicate branch: {}", branch.getBranchId());
            throw new IllegalArgumentException("Branch already exists: " + branch.getBranchId());
        }
        
        branchRepository.save(branch);
        logger.info("Branch registered successfully: {} - {}", branch.getBranchId(), branch.getName());
    }
    
    /**
     * Find a branch by ID
     */
    public Optional<Branch> findBranchById(String branchId) {
        return branchRepository.findById(branchId);
    }
    
    /**
     * Get all branches
     */
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }
    
    /**
     * Update branch information
     */
    public void updateBranch(Branch branch) {
        if (branch == null) {
            throw new IllegalArgumentException("Branch cannot be null");
        }
        
        if (!branchRepository.existsById(branch.getBranchId())) {
            logger.error("Attempted to update non-existent branch: {}", branch.getBranchId());
            throw new IllegalArgumentException("Branch not found: " + branch.getBranchId());
        }
        
        branchRepository.update(branch);
        logger.info("Branch updated successfully: {}", branch.getBranchId());
    }
    
    /**
     * Delete a branch
     */
    public void deleteBranch(String branchId) {
        if (!branchRepository.existsById(branchId)) {
            logger.error("Attempted to delete non-existent branch: {}", branchId);
            throw new IllegalArgumentException("Branch not found: " + branchId);
        }
        
        branchRepository.deleteById(branchId);
        logger.info("Branch deleted successfully: {}", branchId);
    }
    
    /**
     * Check if a branch exists
     */
    public boolean branchExists(String branchId) {
        return branchRepository.existsById(branchId);
    }
}
