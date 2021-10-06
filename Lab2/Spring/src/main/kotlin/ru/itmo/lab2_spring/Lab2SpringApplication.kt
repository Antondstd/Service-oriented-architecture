package ru.itmo.lab2_spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Lab2SpringApplication

fun main(args: Array<String>) {
    runApplication<Lab2SpringApplication>(*args)
}
