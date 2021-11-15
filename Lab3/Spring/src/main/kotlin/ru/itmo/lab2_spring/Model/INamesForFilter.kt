package ru.itmo.lab2_spring.Model

import kotlin.reflect.full.declaredMemberProperties

interface INamesForFilter {
    fun getFieldNames():MutableList<String>{
        val listOfName:MutableList<String> = mutableListOf()
        this::class.declaredMemberProperties.toMutableList().forEach{
            listOfName.add(it.name)
        }
        return listOfName
    }
}