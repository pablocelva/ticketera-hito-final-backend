package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

public class CreateEventUseCase {

    private final EventRepository repository;

    public CreateEventUseCase(EventRepository repository) {
        this.repository = repository;
    }

    public Event execute(Long cityId, String name, String venue, int capacity,
                         String artist, LocalDateTime eventDate, String eventTime,
                         double price, String imageUrl, boolean featured) {
        Event event = new Event(generateEventCode(name), name, venue, capacity,
            artist, eventDate, eventTime, price, imageUrl, featured);
        event.setCityId(cityId);
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
}