package org.librarymanagement.repository;

import org.librarymanagement.mainentities.Branch;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of BranchRepository.
 * Thread-safe using ConcurrentHashMap.
 */
public class InMemoryBranchRepository implements BranchRepository {
    
    private final Map<String, Branch> branches = new ConcurrentHashMap<>();
    
    @Override
    public void save(Branch branch) {
        if (branch == null || branch.getBranchId() == null) {
            throw new IllegalArgumentException("Branch and branchId cannot be null");
        }
        branches.put(branch.getBranchId(), branch);
    }
    
    @Override
    public Optional<Branch> findById(String branchId) {
        return Optional.ofNullable(branches.get(branchId));
    }
    
    @Override
    public List<Branch> findAll() {
        return new ArrayList<>(branches.values());
    }
    
    @Override
    public void update(Branch branch) {
        if (branch == null || branch.getBranchId() == null) {
            throw new IllegalArgumentException("Branch and branchId cannot be null");
        }
        if (!branches.containsKey(branch.getBranchId())) {
            throw new IllegalArgumentException("Branch not found: " + branch.getBranchId());
        }
        branches.put(branch.getBranchId(), branch);
    }
    
    @Override
    public void deleteById(String branchId) {
        branches.remove(branchId);
    }
    
    @Override
    public boolean existsById(String branchId) {
        return branches.containsKey(branchId);
    }
}
