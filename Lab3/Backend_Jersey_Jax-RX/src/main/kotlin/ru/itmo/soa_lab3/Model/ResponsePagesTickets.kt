package ru.itmo.soa_lab3.Model

import java.io.Serializable

class ResponsePagesTickets (
    var currentPage:Int = 1,
    var lastPage:Int = 1,
    var countAll:Long = 1,
    var perPage:Int = 25,
    var tickets:List<Ticket>? = listOf()
):Serializable