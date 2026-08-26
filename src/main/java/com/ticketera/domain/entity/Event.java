package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.CityId;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.Money;
import com.ticketera.domain.valueobject.TicketQuantity;

import java.time.LocalDateTime;

public class Event {
    private Long id;
    private final EventId code;
    private CityId cityId;
    private String name;
    private String venue;
    private int capacity;
    private final TicketPool ticketPool;

    private String artist;
    private LocalDateTime eventDate;
    private String eventTime;
    private Money price;
    private String imageUrl;
    private boolean featured;
    private String status;

    public Event(String code, String name, String venue, int capacity,
                 String artist, LocalDateTime eventDate, String eventTime,
                 double price, String imageUrl, boolean featured) {
        this.code = new EventId(code);
        this.cityId = new CityId(1L);
        this.name = name;
        this.venue = venue;
        this.capacity = capacity;
        this.ticketPool = new TicketPool(capacity);
        this.artist = artist;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.price = new Money(price);
        this.imageUrl = imageUrl;
        this.featured = featured;
        this.status = "SCHEDULED";
    }

    public static Event reconstitute(Long id, EventId code, CityId cityId,
                                     String name, String venue, int capacity, int availableTickets,
                                     String artist, LocalDateTime eventDate, String eventTime,
                                     double price, boolean featured, String status, String imageUrl) {
        return new Event(id, code, cityId, name, venue, capacity, availableTickets,
            artist, eventDate, eventTime, price, featured, status, imageUrl);
    }

    private Event(Long id, EventId code, CityId cityId,
                  String name, String venue, int capacity, int availableTickets,
                  String artist, LocalDateTime eventDate, String eventTime,
                  double price, boolean featured, String status, String imageUrl) {
        this.id = id;
        this.code = code;
        this.cityId = cityId;
        this.name = name;
        this.venue = venue;
        this.capacity = capacity;
        this.ticketPool = new TicketPool(capacity, availableTickets);
        this.artist = artist;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.price = new Money(price);
        this.imageUrl = imageUrl;
        this.featured = featured;
        this.status = status;
    }

    // --- Getters existentes ---

    public Long getDbId() {
        return id;
    }

    public void setDbId(Long id) {
        this.id = id;
    }

    public EventId getCode() {
        return code;
    }

    public CityId getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = new CityId(cityId);
    }

    public String getName() {
        return name;
    }

    public String getVenue() {
        return venue;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean hasAvailability() {
        return ticketPool.hasAvailability();
    }

    public int getAvailableTickets() {
        return ticketPool.getAvailable();
    }

    public int getTicketSold() {
        return capacity - ticketPool.getAvailable();
    }

    public boolean hasSoldTickets() {
        return ticketPool.getAvailable() < capacity;
    }

    public void reserveTickets(TicketQuantity quantity) {
        ticketPool.reserve(quantity);
    }

    // --- Getters nuevos ---

    public String getArtist() {
        return artist;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public Money getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isFeatured() {
        return featured;
    }

    public String getStatus() {
        return status;
    }

    // --- Update detallado ---

    public void updateDetails(String name, String venue, int capacity,
                              String artist, LocalDateTime eventDate, String eventTime,
                              double price, String imageUrl, boolean featured) {
        if (capacity < getTicketSold()) {
            throw new com.ticketera.domain.exception.InvalidOrderException(
                "New capacity (" + capacity + ") cannot be less than sold tickets (" + getTicketSold() + ")");
        }
        this.name = name;
        this.venue = venue;
        this.capacity = capacity;
        this.artist = artist;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.price = new Money(price);
        this.imageUrl = imageUrl;
        this.featured = featured;
    }
}