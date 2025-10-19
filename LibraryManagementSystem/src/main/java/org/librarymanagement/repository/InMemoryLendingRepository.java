package org.librarymanagement.repository;

import org.librarymanagement.mainentities.LendingRecord;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryLendingRepository implements LendingRepository {
    private final Map<String, LendingRecord> lendingRepository;

    public InMemoryLendingRepository() {
        this.lendingRepository = new HashMap<>();
    }

    @Override
    public void add(LendingRecord record) {
        if(record==null){
            throw new IllegalArgumentException("LendingRecord cannot be null");
        }
        lendingRepository.put(record.getRecordId(), record);
    }

    @Override
    public Optional<LendingRecord> findById(String recordId) {
        return Optional.ofNullable(lendingRepository.get(recordId));
    }

    @Override
    public List<LendingRecord> findAll() {
        return new ArrayList<>(lendingRepository.values());
    }

    @Override
    public List<LendingRecord> findByPatronId(String patronId) {
        return lendingRepository.values().stream()
                .filter(record -> record.getPatronId().equals(patronId))
                .collect(Collectors.toList());
    }

    @Override
    public List<LendingRecord> findByIsbn(String isbn) {
        return lendingRepository.values().stream()
                .filter(record -> record.getIsbn().equals(isbn))
                .collect(Collectors.toList());
    }

    @Override
    public void modify(LendingRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("LendingRecord cannot be null");
        }
        lendingRepository.put(record.getRecordId(), record);
    }

    @Override
    public List<LendingRecord> findActiveLendings(String patronId) {
        // finds Active lending records
        return lendingRepository.values().stream().filter(record -> record.getPatronId().equals(patronId) && record.isActive()).collect(Collectors.toList());
    }
}
