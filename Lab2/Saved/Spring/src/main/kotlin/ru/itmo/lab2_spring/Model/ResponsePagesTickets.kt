package ru.itmo.lab2_spring.Model

class ResponsePagesTickets (
    var currentPage:Int = 1,
    var lastPage:Int = 1,
    var countAll:Long = 1,
    var perPage:Int = 25,
    var tickets:List<Ticket>? = listOf()
)