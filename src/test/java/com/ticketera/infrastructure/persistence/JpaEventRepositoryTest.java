package com.ticketera.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.valueobject.CityId;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.EventStatus;
import com.ticketera.domain.valueobject.TicketQuantity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
class JpaEventRepositoryTest {

    @Autowired
    private EventJpaRepository jpaRepository;

    private JpaEventRepository repository;

    @BeforeEach
    void setUp() {
        repository = new JpaEventRepository(jpaRepository);
        jpaRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        jpaRepository.deleteAll();
    }

    @Test
    void persistsAndRecoversAggregateWithAllFields() {
        Event event = Event.reconstitute(null, new EventId("evt-test-1"),
            new CityId(1L), "Jazz Night", "Teatro", 100, 90,
            "Miles Davis", LocalDateTime.of(2026, 12, 1, 20, 0), "20:00",
            25000.0, true, EventStatus.ON_SALE, "/images/jazz.webp");

        repository.save(event);
        Event recovered = repository.findByCode("evt-test-1").orElseThrow();

        assertEquals("Jazz Night", recovered.getName());
        assertEquals(100, recovered.getCapacity());
        assertEquals(90, recovered.getAvailableTickets());
        assertEquals(10, recovered.getTicketSold());
        assertEquals("Miles Davis", recovered.getArtist());
        assertEquals(25000.0, recovered.getPrice().value());
        assertTrue(recovered.isFeatured());
        assertEquals(EventStatus.ON_SALE, recovered.getStatus());
        assertEquals("/images/jazz.webp", recovered.getImageUrl());
        assertEquals(LocalDateTime.of(2026, 12, 1, 20, 0), recovered.getEventDate());
        assertEquals("20:00", recovered.getEventTime());
    }

    @Test
    void persistsReservationsMadeOnAggregate() {
        Event event = Event.reconstitute(null, new EventId("evt-test-2"),
            new CityId(1L), "Rock Fest", "Estadio", 1000, 500,
            "AC/DC", LocalDateTime.now(), "21:00", 50000.0, false, EventStatus.SCHEDULED, "/img.jpg");

        event.reserveTickets(new TicketQuantity(200));
        repository.save(event);
        Event recovered = repository.findByCode("evt-test-2").orElseThrow();

        assertEquals(300, recovered.getAvailableTickets());
        assertEquals(700, recovered.getTicketSold());
    }

    @Test
    void listsAllPersistedEvents() {
        repository.save(Event.reconstitute(null, new EventId("evt-a"),
            new CityId(1L), "A", "V1", 10, 10,
            "ArtA", LocalDateTime.now(), "20:00", 10000.0, false, EventStatus.SCHEDULED, "/img.jpg"));
        repository.save(Event.reconstitute(null, new EventId("evt-b"),
            new CityId(1L), "B", "V2", 20, 15,
            "ArtB", LocalDateTime.now(), "21:00", 20000.0, true, EventStatus.ON_SALE, "/img.jpg"));

        List<Event> all = repository.findAll();

        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(e -> e.getCode().value().equals("evt-b")));
    }
}