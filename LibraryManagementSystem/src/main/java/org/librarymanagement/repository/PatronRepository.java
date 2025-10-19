package org.librarymanagement.repository;

import org.librarymanagement.mainentities.Patron;

import java.util.List;
import java.util.Optional;

public interface PatronRepository {
    void add(Patron patron);
    Optional<Patron> findById(String patronId);
    List<Patron> findAll();
    void modify(Patron patron);
    boolean delete(String patronId);

}
