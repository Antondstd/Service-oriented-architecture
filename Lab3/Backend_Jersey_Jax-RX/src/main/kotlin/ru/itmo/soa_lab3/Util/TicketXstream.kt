package ru.itmo.soa_lab3.Util

import ru.itmo.soa_lab3.Model.Ticket
import com.thoughtworks.xstream.XStream
import ru.itmo.soa_lab3.Model.ResponseAddtitions
import ru.itmo.soa_lab3.Model.ResponsePagesTickets
import ru.itmo.soa_lab3.Model.TicketType

class TicketXstream {
    companion object{
        private var xstream:XStream? = null
        fun getParser():XStream{
            if (xstream == null){
                xstream = XStream()
                xstream!!.alias("Ticket", Ticket::class.java)
                xstream!!.alias("ResponsePagesTickets", ResponsePagesTickets::class.java)
                xstream!!.alias("list", List::class.java)
                xstream!!.alias("TicketType", TicketType::class.java)
                xstream!!.alias("ResponseAdditions", ResponseAddtitions::class.java)
                xstream!!.registerConverter(LocalDateConvertor())
                xstream!!.registerConverter(ZonedDateConvertor())
            }
            return xstream as XStream
        }
    }
}