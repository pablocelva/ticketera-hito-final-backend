package com.ticketera.domain.repository;

import com.ticketera.domain.entity.Ticket;

import java.util.List;

public interface TicketRepository {

    List<Ticket> findByEventId(Long eventId);

    List<Ticket> findByCustomerEmail(String customerEmail);

    void save(Ticket ticket);
}
