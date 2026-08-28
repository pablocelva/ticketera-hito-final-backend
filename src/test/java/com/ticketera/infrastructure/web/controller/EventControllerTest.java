package com.ticketera.infrastructure.web.controller;

import com.ticketera.application.usecase.CreateEventUseCase;
import com.ticketera.application.usecase.DeleteEventUseCase;
import com.ticketera.application.usecase.GetEventDetailsUseCase;
import com.ticketera.application.usecase.GetEventTicketsUseCase;
import com.ticketera.application.usecase.GetEventsUseCase;
import com.ticketera.application.usecase.UpdateEventUseCase;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.valueobject.CityId;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.EventStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Event Controller")
@WebMvcTest(EventController.class)
@AutoConfigureMockMvc(addFilters = false)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetEventsUseCase getEventsUseCase;

    @MockitoBean
    private GetEventDetailsUseCase getEventDetailsUseCase;

    @MockitoBean
    private CreateEventUseCase createEventUseCase;

    @MockitoBean
    private UpdateEventUseCase updateEventUseCase;

    @MockitoBean
    private DeleteEventUseCase deleteEventUseCase;

    @MockitoBean
    private GetEventTicketsUseCase getEventTicketsUseCase;

    private Event sampleEvent() {
        return Event.reconstitute(1L, new EventId("evt-1"),
            new CityId(1L), "Jazz Night", "Teatro", 100, 90,
            "Miles Davis", LocalDateTime.of(2026, 12, 1, 20, 0), "20:00",
            25000.0, true, EventStatus.ON_SALE, "/images/jazz.webp");
    }

    @Test
    @DisplayName("Lists all events")
    void listsAllEvents() throws Exception {
        when(getEventsUseCase.execute()).thenReturn(List.of(sampleEvent()));

        mockMvc.perform(get("/api/v1/events"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].code").value("evt-1"))
            .andExpect(jsonPath("$[0].name").value("Jazz Night"))
            .andExpect(jsonPath("$[0].availableTickets").value(90))
            .andExpect(jsonPath("$[0].ticketsSold").value(10))
            .andExpect(jsonPath("$[0].artist").value("Miles Davis"))
            .andExpect(jsonPath("$[0].price").value(25000.0))
            .andExpect(jsonPath("$[0].imageUrl").value("/images/jazz.webp"))
            .andExpect(jsonPath("$[0].featured").value(true))
            .andExpect(jsonPath("$[0].status").value("ON_SALE"));
    }

    @Test
    @DisplayName("Returns event by id")
    void returnsEventById() throws Exception {
        when(getEventDetailsUseCase.execute(1L)).thenReturn(sampleEvent());

        mockMvc.perform(get("/api/v1/events/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.code").value("evt-1"))
            .andExpect(jsonPath("$.artist").value("Miles Davis"))
            .andExpect(jsonPath("$.price").value(25000.0));
    }

    @Test
    @DisplayName("Returns 404 when event not found")
    void returns404WhenEventNotFound() throws Exception {
        when(getEventDetailsUseCase.execute(999L))
            .thenThrow(new EventNotFoundException("Event not found: 999"));

        mockMvc.perform(get("/api/v1/events/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404))
            .andExpect(jsonPath("$.message").value("Event not found: 999"))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Creates event and returns 201")
    void createsEventAndReturns201() throws Exception {
        when(createEventUseCase.execute(anyLong(), any(), any(), anyInt(),
        any(), any(LocalDateTime.class), any(), anyDouble(), any(), anyBoolean(), any()))
    .thenReturn(Event.reconstitute(1L, new EventId("evt-new"),
        new CityId(1L), "Rock Fest", "Estadio", 1000, 1000,
        "AC/DC", LocalDateTime.of(2027, 1, 15, 21, 0), "21:00",
        50000.0, false, EventStatus.SCHEDULED, "/images/rock.webp"));

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "cityId": 1,
                      "name": "Rock Fest",
                      "venue": "Estadio",
                      "capacity": 1000,
                      "artist": "AC/DC",
                      "eventDate": "2027-01-15T21:00:00",
                      "eventTime": "21:00",
                      "price": 50000.0,
                      "imageUrl": "/images/rock.webp",
                      "featured": false,
                      "status": "SCHEDULED"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("SCHEDULED"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.code").value("evt-new"))
            .andExpect(jsonPath("$.artist").value("AC/DC"))
            .andExpect(jsonPath("$.price").value(50000.0))
            .andExpect(jsonPath("$.availableTickets").value(1000));
    }

    @Test
    @DisplayName("Returns 400 when create body is invalid")
    void returns400WhenCreateBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name": "", "venue": "", "capacity": -5}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @DisplayName("Updates event and returns 200")
    void updatesEventAndReturns200() throws Exception {
        when(updateEventUseCase.execute(eq(1L), any(), any(), anyInt(),
        any(), any(LocalDateTime.class), any(), anyDouble(), any(), anyBoolean(), any()))
            .thenReturn(Event.reconstitute(1L, new EventId("evt-1"),
                new CityId(1L), "Rock Night", "Estadio", 500, 500,
                "Bands", LocalDateTime.of(2027, 6, 1, 20, 0), "20:00",
                30000.0, false, EventStatus.ON_SALE, "/images/rock.webp"));

        mockMvc.perform(put("/api/v1/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Rock Night",
                      "venue": "Estadio",
                      "capacity": 500,
                      "artist": "Bands",
                      "eventDate": "2027-06-01T20:00:00",
                      "eventTime": "20:00",
                      "price": 30000.0,
                      "imageUrl": "/images/rock.webp",
                      "featured": false,
                      "status": "ON_SALE"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Rock Night"))
            .andExpect(jsonPath("$.artist").value("Bands"))
            .andExpect(jsonPath("$.status").value("ON_SALE"))
            .andExpect(jsonPath("$.availableTickets").value(500));
    }

    @Test
    @DisplayName("Returns 404 when updating non-existent event")
    void returns404WhenUpdatingNonExistentEvent() throws Exception {
        when(updateEventUseCase.execute(eq(999L), any(), any(), anyInt(),
        any(), any(LocalDateTime.class), any(), anyDouble(), any(), anyBoolean(), any()))
            .thenThrow(new EventNotFoundException("Event not found: 999"));

        mockMvc.perform(put("/api/v1/events/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Rock Night",
                      "venue": "Estadio",
                      "capacity": 500,
                      "artist": "Bands",
                      "eventDate": "2027-06-01T20:00:00",
                      "eventTime": "20:00",
                      "price": 30000.0,
                      "imageUrl": "/images/rock.webp",
                      "featured": false
                    }
                    """))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @DisplayName("Deletes event and returns 204")
    void deletesEventAndReturns204() throws Exception {
        doNothing().when(deleteEventUseCase).execute(1L);

        mockMvc.perform(delete("/api/v1/events/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Returns 404 when deleting non-existent event")
    void returns404WhenDeletingNonExistentEvent() throws Exception {
        org.mockito.Mockito.doThrow(new EventNotFoundException("Event not found: 999"))
            .when(deleteEventUseCase).execute(999L);

        mockMvc.perform(delete("/api/v1/events/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404));
    }
}