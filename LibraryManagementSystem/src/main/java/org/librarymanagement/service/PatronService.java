package org.librarymanagement.service;

import java.util.List;
import java.util.Optional;

import org.librarymanagement.mainentities.Patron;
import org.librarymanagement.repository.PatronRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatronService {
    private static final Logger logger = LoggerFactory.getLogger(PatronService.class);
    private final PatronRepository patronRepository;

    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    public void addPatron(Patron patron) {
        try {
            patronRepository.add(patron);
            logger.info("Patron added successfully: {}", patron.getPatronId());
        } catch (Exception e) {
           logger.error("Error adding patron: {}", patron.getPatronId(), e);
            throw e;
        }
    }

    public void updatePatron(Patron patron) {
        try {
            patronRepository.modify(patron);
            logger.info("Patron updated successfully: {}", patron.getPatronId());
        } catch (Exception e) {
            logger.error("Error updating patron: {}", patron.getPatronId(), e);
            throw e;
        }
    }

    public boolean removePatron(String patronId) {
        try {
            boolean removed = patronRepository.delete(patronId);
            if (removed) {
               logger.info("Patron removed successfully: {}", patronId);
            } else {
               logger.warn("Patron not found for removal: {}", patronId);
            }
            return removed;
        } catch (Exception e) {
             logger.error("Error removing patron: {}", patronId, e);
            throw e;
        }
    }

    public Optional<Patron> findPatronById(String patronId) {
        return patronRepository.findById(patronId);
    }

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }
}
