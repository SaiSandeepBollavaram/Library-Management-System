package org.librarymanagement.repository;


import org.librarymanagement.mainentities.Patron;

import java.util.*;


public class InMemoryPatronRepository implements PatronRepository {

    private final Map<String, Patron> patronRepository = new HashMap<>();

    public InMemoryPatronRepository() {}

    @Override
    public void add(Patron patron) {
        if (patron == null) {
            throw new IllegalArgumentException("Patron cannot be null");
        }
        if (patronRepository.containsKey(patron.getPatronId())) {
            throw new IllegalStateException("Patron with ID " + patron.getPatronId() + " already exists");
        }
        patronRepository.put(patron.getPatronId(), patron);
    }

    @Override
    public Optional<Patron> findById(String patronId) {
        return Optional.ofNullable(patronRepository.get(patronId));
    }

    @Override    public List<Patron> findAll() {
        return new ArrayList<>(patronRepository.values());
    }

    @Override
    public void modify(Patron patron) {
        if (patron == null) {
            throw new IllegalArgumentException("Patron cannot be null");
        }
        if (!patronRepository.containsKey(patron.getPatronId())) {
            throw new IllegalStateException("Patron with ID " + patron.getPatronId() + " does not exist");
        }
        patronRepository.put(patron.getPatronId(), patron);
    }

    @Override
    public boolean delete(String patronId) {
        return patronRepository.remove(patronId) != null;
    }

}
