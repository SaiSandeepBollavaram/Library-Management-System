package org.librarymanagement.repository;

import org.librarymanagement.mainentities.LendingRecord;

import java.util.List;
import java.util.Optional;

public interface LendingRepository {
    void add(LendingRecord record);
    Optional<LendingRecord> findById(String recordId);
    List<LendingRecord> findAll();
    List<LendingRecord> findByPatronId(String patronId);
    List<LendingRecord> findByIsbn(String isbn);
    void modify(LendingRecord record);
    List<LendingRecord> findActiveLendings(String patronId);
}

