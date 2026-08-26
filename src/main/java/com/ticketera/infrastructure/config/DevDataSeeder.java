package com.ticketera.infrastructure.config;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.CityRepository;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.TicketQuantity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

@Configuration
@Profile("dev")
public class DevDataSeeder {

    @Bean
    CommandLineRunner seedData(CityRepository cityRepository, EventRepository eventRepository) {
        return args -> {
            if (cityRepository.findAll().isEmpty()) {
                cityRepository.save(new City(null, "LIM", "Lima"));
                cityRepository.save(new City(null, "BOG", "Bogota"));
                cityRepository.save(new City(null, "MAD", "Madrid"));
            }

            if (eventRepository.findAll().isEmpty()) {
                var limaId = cityRepository.findByCode("LIM").map(c -> c.getId().value()).orElse(1L);
                var bogId = cityRepository.findByCode("BOG").map(c -> c.getId().value()).orElse(2L);
                var madId = cityRepository.findByCode("MAD").map(c -> c.getId().value()).orElse(3L);

                var jazz = new Event(
                    "evt-jazz-001", "Jazz Night", "Gran Teatro Lima", 500,
                    "Miles Davis Quartet", LocalDateTime.of(2026, 12, 15, 20, 0), "20:00",
                    25000.0, "/images/braxton.webp", true);
                jazz.setCityId(limaId);

                var rock = new Event(
                    "evt-rock-002", "Rock Fest", "Estadio Nacional", 5000,
                    "AC/DC", LocalDateTime.of(2027, 3, 20, 21, 0), "21:00",
                    55000.0, "/images/elena.webp", false);
                rock.setCityId(limaId);
                rock.reserveTickets(new TicketQuantity(1200));

                var opera = new Event(
                    "evt-opera-003", "La Traviata", "Teatro Real Madrid", 800,
                    "Placido Domingo", LocalDateTime.of(2027, 1, 10, 19, 0), "19:00",
                    120000.0, "/images/opera.webp", true);
                opera.setCityId(madId);
                opera.reserveTickets(new TicketQuantity(800));

                var festival = new Event(
                    "evt-fest-004", "Bogota Music Festival", "Parque Simon Bolivar", 10000,
                    "Various Artists", LocalDateTime.of(2027, 6, 1, 14, 0), "14:00",
                    80000.0, "/images/festival.webp", false);
                festival.setCityId(bogId);

                eventRepository.save(jazz);
                eventRepository.save(rock);
                eventRepository.save(opera);
                eventRepository.save(festival);
            }
        };
    }
}