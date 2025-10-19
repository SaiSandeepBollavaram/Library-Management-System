package org.librarymanagement.mainentities;

import java.util.UUID;

public class PatronFactory {
    public static Patron createStudent(String name, String email, String phoneNumber) {
        String patronId = generatePatronId("STU");
        return new Patron(patronId, name, email, phoneNumber, PatronType.STUDENT);
    }

    public static Patron createFaculty(String name, String email, String phoneNumber) {
        String patronId = generatePatronId("FAC");
        return new Patron(patronId, name, email, phoneNumber, PatronType.FACULTY);
    }

    private static String generatePatronId(String prefix) {
        return prefix + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}