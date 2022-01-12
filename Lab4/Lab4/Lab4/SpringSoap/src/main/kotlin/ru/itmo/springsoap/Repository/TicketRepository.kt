package ru.itmo.springsoap.Repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.itmo.springsoap.Model.Ticket

interface TicketRepository: JpaRepository<Ticket, Long> {
}