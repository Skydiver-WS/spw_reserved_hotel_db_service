package ru.project.reserved.system.db.app.service.exception;

import org.springframework.data.crossstore.ChangeSetPersister;

public class BookingException extends NullPointerException {
    public BookingException(String message) {
        super(message);
    }
}
