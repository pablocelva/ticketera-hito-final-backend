package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;
import com.ticketera.domain.valueobject.EventStatus;

public class CreateEventUseCase {

    private final EventRepository repository;

    public CreateEventUseCase(EventRepository repository) {
        this.repository = repository;
    }

    public Event execute(Long cityId, String name, String venue, int capacity,
                     String artist, LocalDateTime eventDate, String eventTime,
                     double price, String imageUrl, boolean featured, EventStatus status) {
        Event event = new Event(generateEventCode(name), name, venue, capacity,
            artist, eventDate, eventTime, price, imageUrl, featured);
        event.setCityId(cityId);
        applyStatus(event, status);
        Long id = repository.save(event);
        event.setDbId(id);
        return event;
    }

    private String generateEventCode(String name) {
        String slug = name.toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-|-$", "");
        return "evt-" + slug + "-" + UUID.randomUUID().toString().substring(0, 6);
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