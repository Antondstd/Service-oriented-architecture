package ru.itmo.springsoap.Utils

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.xml.bind.annotation.adapters.XmlAdapter

class JaxBLocalDateTimeAdapter: XmlAdapter<String, LocalDateTime>()  {
    override fun unmarshal(v: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yy")
        return LocalDateTime.parse(v, formatter)
    }

    override fun marshal(v: LocalDateTime): String {
        return v.format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yy"))
    }
}