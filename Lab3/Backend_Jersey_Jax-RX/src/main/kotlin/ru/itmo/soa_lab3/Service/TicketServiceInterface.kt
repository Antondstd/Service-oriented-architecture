package ru.itmo.soa_lab3.Service


import ru.itmo.soa_lab3.Model.ResponseGroupedDiscount
import ru.itmo.soa_lab3.Model.ResponsePagesTickets
import ru.itmo.soa_lab3.Model.Ticket
import ru.itmo.soa_lab3.Model.TicketType
import javax.ejb.Remote

@Remote
interface TicketServiceInterface {
    fun getTicketsSortPaging(
        page: Int = 1,
        perPage: Int = 25,
        sortStateList: List<String>?,
        filterMap: Map<String, MutableList<String>>
    ): ResponsePagesTickets?
    fun addTicketFromXml(xml:String?): Ticket?
    fun updateTicketFromXml(xml: String?,id:Long)
    fun deleteTicket(ticketId: Long)
    fun getGroupedByDiscount(): MutableList<ResponseGroupedDiscount>?
    fun getDistinctTypes(): MutableList<Any>?
    fun deleteSomeTicketByType(type: TicketType): Long
    fun getTicket(id: Long): Ticket?
}