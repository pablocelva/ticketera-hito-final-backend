package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.valueobject.CityId;
import com.ticketera.domain.valueobject.EventId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "city_id", nullable = false)
    private Long cityId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "venue", nullable = false)
    private String venue;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "available_tickets", nullable = false)
    private int availableTickets;

    @Column(name = "artist")
    private String artist;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "event_time", length = 10)
    private String eventTime;

    @Column(name = "price")
    private Double price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "featured", nullable = false)
    private boolean featured;

    @Column(name = "status", length = 20)
    private String status;

    protected EventEntity() {
    }

    public static EventEntity fromDomain(Event event) {
        return new EventEntity(
            event.getDbId(),
            event.getCode().value(),
            event.getCityId().value(),
            event.getName(),
            event.getVenue(),
            event.getCapacity(),
            event.getAvailableTickets(),
            event.getArtist(),
            event.getEventDate(),
            event.getEventTime(),
            event.getPrice().value(),
            event.getImageUrl(),
            event.isFeatured(),
            event.getStatus());
    }

    private EventEntity(Long id, String code, Long cityId, String name, String venue,
                        int capacity, int availableTickets, String artist,
                        LocalDateTime eventDate, String eventTime, Double price,
                        String imageUrl, boolean featured, String status) {
        this.id = id;
        this.code = code;
        this.cityId = cityId;
        this.name = name;
        this.venue = venue;
        this.capacity = capacity;
        this.availableTickets = availableTickets;
        this.artist = artist;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.price = price;
        this.imageUrl = imageUrl;
        this.featured = featured;
        this.status = status;
    }

    public Event toDomain() {
        return Event.reconstitute(
            id, new EventId(code), new CityId(cityId),
            name, venue, capacity, availableTickets,
            artist, eventDate, eventTime,
            price, featured, status, imageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
}