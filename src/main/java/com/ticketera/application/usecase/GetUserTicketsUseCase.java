package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Ticket;
import com.ticketera.domain.repository.TicketRepository;

import java.util.List;

public class GetUserTicketsUseCase {

    private final TicketRepository ticketRepository;

    public GetUserTicketsUseCase(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> execute(String email) {
        if (email == null || email.isBlank()) {
            return List.of();
        }
        return ticketRepository.findByCustomerEmail(email);
    }
}

