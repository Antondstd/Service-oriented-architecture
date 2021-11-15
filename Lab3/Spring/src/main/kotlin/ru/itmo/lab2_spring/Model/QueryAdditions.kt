package ru.itmo.lab2_spring.Model

class QueryAdditions(
    val table: TablesQueryAdditions,
    val column:String,
    val firstValue:String? = null,
    val secondValue:String? = null,
    val sortType:Boolean = true // asc == true, desc == false
) {
}