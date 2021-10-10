package ru.itmo.lab2_spring.Repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.itmo.lab2_spring.Model.Coordinates

interface CoordinatesRepository :JpaRepository<Coordinates,Long> {
}