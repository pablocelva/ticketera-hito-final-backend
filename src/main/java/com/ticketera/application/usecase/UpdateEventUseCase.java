package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventStatus;

import java.time.LocalDateTime;

public class UpdateEventUseCase {

    private final EventRepository repository;

    public UpdateEventUseCase(EventRepository repository) {
        this.repository = repository;
    }

    public Event execute(Long eventId, String name, String venue, int capacity,
                         String artist, LocalDateTime eventDate, String eventTime,
                         double price, String imageUrl, boolean featured, EventStatus status) {
        Event event = repository.findById(eventId)
            .orElseThrow(() -> new EventNotFoundException("Event not found: " + eventId));

        event.updateDetails(name, venue, capacity, artist, eventDate, eventTime,
            price, imageUrl, featured);
        applyStatus(event, status);
        repository.save(event);
        return event;
    }

    private void applyStatus(Event event, EventStatus status) {
        if (status == null) {
            return;
        }
        switch (status) {
            case ON_SALE -> event.markOnSale();
            case SOLD_OUT -> event.markSoldOut();
            case CANCELED -> event.markCanceled();
            default -> { /* SCHEDULED, LIVE, FINISHED — dejar default o ignorar */ }
        }
    }
}