package ru.itmo.soa_lab3.Util

import ru.itmo.soa_lab3.Exceptions.BadRequestException
import ru.itmo.soa_lab3.Model.Ticket
import ru.itmo.soa_lab3.Model.TicketType

class ParamsChecker {
    companion object{

        fun getAndCheckStringToPositiveLong(name:String,numberS:String?):Long{
            val number:Long
            try {
                number = numberS!!.toLong()
            }
            catch (e:Exception){
                throw BadRequestException("$name должно быть числом")
            }
            if (number < 1)
                throw BadRequestException("$name должно быть >= 1")
            return number
        }

        fun checkSort(sortType:String,columnName:String){
            if (sortType != "asc" && sortType != "desc")
                throw BadRequestException("У сортировки должен быть параметр desc или asc")
            if (!Ticket.getNamesForFilter().contains(columnName))
                throw BadRequestException("Для сортировки был указан неверный столбец $columnName")
        }

        fun getAndCheckStringToTicketType(type:String):TicketType{
            try {
                return TicketType.valueOf(type)
            }
            catch (e:Exception){
                throw BadRequestException("Нет такого типа \"$type\" у класса Ticket")
            }
        }
        fun getAndcheckIfPathHasID(path:String?):Long{
            if (path == null)
                throw BadRequestException("В пути должно присутсвовать id Ticket-а")
             return getAndCheckStringToPositiveLong("id",path.drop(1))
        }
    }
}