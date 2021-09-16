package ru.itmo.SOA_Backend_Lab1.Service

import ru.itmo.SOA_Backend_Lab1.DAO.TicketDAO
import ru.itmo.SOA_Backend_Lab1.Exceptions.BadRequestException
import ru.itmo.SOA_Backend_Lab1.Exceptions.UnprocessableEntityException
import ru.itmo.SOA_Backend_Lab1.Model.QueryAdditions
import ru.itmo.SOA_Backend_Lab1.Model.ResponsePagesTickets
import ru.itmo.SOA_Backend_Lab1.Model.TablesQueryAdditions
import ru.itmo.SOA_Backend_Lab1.Model.Ticket
import ru.itmo.SOA_Backend_Lab1.Util.ParamsChecker
import ru.itmo.SOA_Backend_Lab1.Util.TicketXstream


class TicketService() : TicketDAO() {


    fun `get Ticket with sorting,paging and filtering`(
        page: Int = 1,
        perPage: Int = 25,
        sortStateList: List<String>?,
        filterMap: Map<String, Array<String>>
    ): ResponsePagesTickets? {

        val tickets: List<Ticket>?
        val regExParam = "^(\\w+)\\(([\\w]+)\\)\$".toRegex()
        val regExName = "^(\\w+).(\\w+)$".toRegex()
        val sortQueryAdditions: MutableList<QueryAdditions> = mutableListOf()
        val filerQueryAdditions: MutableList<QueryAdditions> = mutableListOf()


        sortStateList?.forEach {
            println("SORT STATE : $it")
            val values: List<String>? = regExParam.find(it)?.groupValues //value(1) -> sortType, (2) -> table.col or col
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
//                    sortMap[values[2]] = Pair("ticket", values[1])
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
//                        sortMap.put(
//                            classAndValue.groupValues[2],
//                            Pair(classAndValue.groupValues[1], values[1])
//                        )
                    }
                }
            }
            else{
                throw BadRequestException("Был указан неверный паарметр для сортировки $it")
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
                else -> TablesQueryAdditions.TICKET // TODO добавить проверку имен таблиц
            }
            filerQueryAdditions.add(QueryAdditions(table, name[1], value[0], if (value.size > 1) value[1] else null))
        }
        val pairCountMessage = getCountTicket(filerQueryAdditions)
        if (pairCountMessage.second != null){
            throw Exception(pairCountMessage.second) // TODO тут че-то странное я делаю
        }
        val count = pairCountMessage.first
        if (count == 0L)
            return null
        println("COUNT = $count")
        var pageNumber: Int
        var curPage:Int = page
        if (count != null)
            pageNumber = Math.ceil(count.toDouble() / perPage).toInt()
        else
            return null
        if (page > pageNumber)
            curPage = pageNumber
//            tickets = getSortedFilteredTickets(page,perPage,sortMap,filterMap)
        val pairTicketsMessage:Pair<List<Ticket>?, String?>
        try {
            pairTicketsMessage = getSortedFilteredTickets(curPage, perPage, sortQueryAdditions, filerQueryAdditions)
        }
        catch (e:Exception){
            throw e
        }
        if (pairCountMessage.second != null){
            throw Exception(pairCountMessage.second)
        }
        tickets = pairTicketsMessage.first
        println(tickets)
        return ResponsePagesTickets(currentPage = curPage, perPage = perPage, tickets = tickets, countAll = count)
    }

    fun addTicketFromXml(xml:String?):Ticket?{
        val xstream = TicketXstream.getParser()
        val newTicket: Ticket
        try {
            newTicket = xstream.fromXML(xml) as Ticket
        } catch (exception: Exception) {
            throw UnprocessableEntityException("Невалидный xml")
        }
        newTicket.checkConstrains()
        return addTicket(newTicket)
    }

    fun updateTicketFromXml(xml: String?,id:Long){
        val xstream = TicketXstream.getParser()
        val newTicket: Ticket
        try {
            newTicket = xstream.fromXML(xml) as Ticket
        } catch (exception: Exception) {
            throw UnprocessableEntityException("Невалидный xml")
        }
        newTicket.id = id
        newTicket.checkConstrains()
        val oldTicket = getTicket(id)
        if (oldTicket != null) {
            newTicket.creationDate = oldTicket.creationDate
            newTicket.id = oldTicket.id
            updateTicket(newTicket)
        }

    }

    fun test() {
        println("TEST")
    }
}