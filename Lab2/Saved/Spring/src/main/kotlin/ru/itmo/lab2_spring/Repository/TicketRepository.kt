package ru.itmo.lab2_spring.Repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.itmo.lab2_spring.Model.Ticket

interface TicketRepository: JpaRepository<Ticket, Long> {
}