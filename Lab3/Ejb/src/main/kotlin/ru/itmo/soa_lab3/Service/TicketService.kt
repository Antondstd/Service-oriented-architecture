package ru.itmo.soa_lab3.Service

import ru.itmo.soa_lab3.DAO.TicketDAO
import ru.itmo.soa_lab3.Exceptions.BadRequestException
import ru.itmo.soa_lab3.Exceptions.UnprocessableEntityException
import ru.itmo.soa_lab3.Model.*
import ru.itmo.soa_lab3.Util.ParamsChecker
import ru.itmo.soa_lab3.Util.TicketXstream
import java.time.ZonedDateTime
import javax.ejb.Stateless


@Stateless
//@TransactionManagement(TransactionManagementType.BEAN)
class TicketService() : TicketServiceInterface {

    val ticketDao = TicketDAO()


    override fun getTicketsSortPaging(
        page: Int,
        perPage: Int,
        sortStateList: List<String>?,
        filterMap: Map<String, MutableList<String>>
    ): ResponsePagesTickets? {

        val tickets: List<Ticket>?
        val regExParam = "^(\\w+)\\(([\\w.]+)\\)\$".toRegex()
        val regExName = "^(\\w+).(\\w+)$".toRegex()
        val sortQueryAdditions: MutableList<QueryAdditions> = mutableListOf()
        val filerQueryAdditions: MutableList<QueryAdditions> = mutableListOf()


        sortStateList?.forEach {
            val values: List<String>? = regExParam.find(it)?.groupValues
            if (values != null) {
                ParamsChecker.checkSort(values[1],values[2])
                if (!values[2].contains("."))
                    sortQueryAdditions.add(
                        QueryAdditions(
                            TablesQueryAdditions.TICKET,
                            values[2],
                            sortType = values[1] == "asc"
                        )
                    )
                else {
                    val classAndValue = regExName.find(values[2])
                    val table = when (classAndValue?.groupValues?.get(1)) {
                        "coordinates" -> TablesQueryAdditions.COORDINATES
                        "event" -> TablesQueryAdditions.EVENT
                        else -> null
                    }
                    if (table != null && classAndValue != null) {
                        sortQueryAdditions.add(
                            QueryAdditions(
                                table,
                                classAndValue.groupValues[2],
                                sortType = values[1] == "asc"
                            )
                        )
                    }
                }
            }
            else{
                throw BadRequestException("Был указан неверный параметр для сортировки $it")
            }
        }

        filterMap.forEach { key, value ->
            var name = mutableListOf<String>()
            if (key.contains("."))
                name = key.split(".").toMutableList()
            else {
                name.add(0, "ticket")
                name.add(1, key)
            }

            val table = when (name[0]) {
                "coordinates" -> TablesQueryAdditions.COORDINATES
                "event" -> TablesQueryAdditions.EVENT
                else -> TablesQueryAdditions.TICKET
            }
            filerQueryAdditions.add(QueryAdditions(table, name[1], value[0], if (value.size > 1) value[1] else null))
        }
        val count = ticketDao.getCountTicket(filerQueryAdditions)
        if (count == 0L)
            return null
        var pageNumber: Int
        var curPage:Int = page

        pageNumber = Math.ceil(count.toDouble() / perPage).toInt()
        if (page > pageNumber)
            curPage = pageNumber
        val pairTicketsMessage:Pair<List<Ticket>?, String?>
        try {
            pairTicketsMessage = ticketDao.getSortedFilteredTickets(curPage, perPage, sortQueryAdditions, filerQueryAdditions)
        }
        catch (e:Exception){
            throw e
        }
        tickets = pairTicketsMessage.first
        println(tickets)
        return ResponsePagesTickets(currentPage = curPage, perPage = perPage, tickets = tickets, countAll = count)
    }

    override fun addTicketFromXml(xml:String?):Ticket?{
        val xstream = TicketXstream.getParser()
        val newTicket: Ticket
        try {
            newTicket = xstream.fromXML(xml) as Ticket
        } catch (exception: Exception) {
            throw UnprocessableEntityException("Невалидный xml")
        }
        newTicket.creationDate = ZonedDateTime.now().withNano(0)
        println("ADDING TICKET WITH ZONED TIME ${newTicket.creationDate.toString()}")
        newTicket.id = 0
        newTicket.event?.id = 0
        newTicket.coordinates?.id = 0
        newTicket.checkConstrains()
        return ticketDao.addTicket(newTicket)
    }

    override fun updateTicketFromXml(xml: String?, id:Long){
        val xstream = TicketXstream.getParser()
        val newTicket: Ticket
        try {
            newTicket = xstream.fromXML(xml) as Ticket
        } catch (exception: Exception) {
            throw UnprocessableEntityException("Невалидный xml")
        }
        newTicket.id = id
        newTicket.checkConstrains()
        val oldTicket = ticketDao.getTicket(id)
        if (oldTicket != null) {
            newTicket.creationDate = oldTicket.creationDate
            newTicket.id = oldTicket.id
            newTicket.event?.id  = oldTicket.event?.id!!
            newTicket.coordinates?.id = oldTicket.coordinates?.id!!
            ticketDao.updateTicket(newTicket)
        }

    }

    override fun deleteTicket(ticketId: Long) {
        ticketDao.deleteTicket(ticketId)
    }

    override fun getGroupedByDiscount(): MutableList<ResponseGroupedDiscount>? {
        return ticketDao.getGroupedByDiscount()
    }

    override fun getDistinctTypes(): MutableList<Any>? {
        return ticketDao.getDistinctTypes()
    }

    override fun deleteSomeTicketByType(type: TicketType): Long {
        return ticketDao.deleteSomeTicketByType(type)
    }

    override fun getTicket(id: Long): Ticket? {
        return ticketDao.getTicket(id)
    }

    fun test() {
        println("TEST")
    }
}