package com.ticketera.infrastructure.config;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.entity.User;
import com.ticketera.domain.repository.CityRepository;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.repository.UserRepository;
import com.ticketera.domain.valueobject.Role;
import com.ticketera.domain.valueobject.TicketQuantity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@Profile("dev")
public class DevDataSeeder {

    @Bean
    CommandLineRunner seedData(CityRepository cityRepository, EventRepository eventRepository,
                               UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@ticketera.com").isEmpty()) {
                userRepository.save(new User(null,
                    "admin@ticketera.com",
                    "Administrador Ticketera",
                    passwordEncoder.encode("admin123"),
                    Role.ROLE_ADMIN,
                    LocalDateTime.now()));
            }

            if (userRepository.findByEmail("user@ticketera.com").isEmpty()) {
                userRepository.save(new User(null,
                    "user@ticketera.com",
                    "Usuario Cliente",
                    passwordEncoder.encode("user123"),
                    Role.ROLE_USER,
                    LocalDateTime.now()));
            }

            if (cityRepository.findAll().isEmpty()) {
                cityRepository.save(new City(null, "SCL", "Santiago"));
                cityRepository.save(new City(null, "VAP", "Valparaíso"));
                cityRepository.save(new City(null, "VDA", "Valdivia"));
            }

            if (eventRepository.findAll().isEmpty()) {
                var sclId = cityRepository.findByCode("SCL").map(c -> c.getId().value()).orElse(1L);
                var vapId = cityRepository.findByCode("VAP").map(c -> c.getId().value()).orElse(2L);
                var vdaId = cityRepository.findByCode("VDA").map(c -> c.getId().value()).orElse(3L);

                var braxton = new Event(
                        "evt-braxton-001", "Braxton Cook Live", "Teatro Nescafé de las Artes", 800,
                        "Braxton Cook", LocalDateTime.of(2026, 12, 15, 20, 0), "20:00",
                        45000.0, "/images/braxton.webp", true);
                braxton.setCityId(sclId);
                braxton.markOnSale();

                var elena = new Event(
                        "evt-elena-002", "Elena Pinderhughes Quintet", "Centro Cultural Gabriela Mistral", 600,
                        "Elena Pinderhughes", LocalDateTime.of(2027, 1, 20, 21, 0), "21:00",
                        55000.0, "/images/elena.webp", true);
                elena.setCityId(sclId);
                elena.reserveTickets(new TicketQuantity(150));

                var internet = new Event(
                        "evt-internet-003", "The Internet - Hive Mind Tour", "Teatro Caupolicán", 4500,
                        "The Internet", LocalDateTime.of(2027, 3, 10, 20, 0), "20:00",
                        65000.0, "/images/internet.jfif", true);
                internet.setCityId(vapId);
                internet.reserveTickets(new TicketQuantity(500));

                var genevieve = new Event(
                        "evt-genevieve-004", "Genevieve Artadi Solo", "Teatro del Lago", 1000,
                        "Genevieve Artadi", LocalDateTime.of(2027, 2, 14, 19, 30), "19:30",
                        48000.0, "/images/genevieve.jfif", false);
                genevieve.setCityId(vdaId);
                genevieve.reserveTickets(new TicketQuantity(200));

                var jazmin = new Event(
                        "evt-jazmin-005", "Jazmin Sullivan - Heaux Tales", "Movistar Arena", 12000,
                        "Jazmin Sullivan", LocalDateTime.of(2027, 4, 5, 21, 0), "21:00",
                        75000.0, "/images/jazmin.jfif", true);
                jazmin.setCityId(sclId);
                jazmin.reserveTickets(new TicketQuantity(3000));

                var louis = new Event(
                        "evt-louis-006", "Louis Cole - Quality Over Opinion", "Centro de Convenciones", 2000,
                        "Louis Cole", LocalDateTime.of(2027, 5, 12, 20, 0), "20:00",
                        50000.0, "/images/louis.jpg", false);
                louis.setCityId(vapId);
                louis.reserveTickets(new TicketQuantity(300));

                var terrace = new Event(
                        "evt-terrace-007", "Terrace Martin - Velvet Portraits", "Teatro Municipal", 1500,
                        "Terrace Martin", LocalDateTime.of(2027, 6, 21, 19, 0), "19:00",
                        52000.0, "/images/terrace.jpg", true);
                terrace.setCityId(vdaId);
                terrace.reserveTickets(new TicketQuantity(100));

                eventRepository.save(braxton);
                eventRepository.save(elena);
                eventRepository.save(internet);
                eventRepository.save(genevieve);
                eventRepository.save(jazmin);
                eventRepository.save(louis);
                eventRepository.save(terrace);
            }
        };
    }
}