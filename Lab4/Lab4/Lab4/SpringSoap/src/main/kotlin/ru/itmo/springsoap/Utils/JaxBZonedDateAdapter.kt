package ru.itmo.springsoap.Utils

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.xml.bind.annotation.adapters.XmlAdapter

class JaxBZonedDateAdapter: XmlAdapter<String, ZonedDateTime>() {
    override fun unmarshal(v: String): ZonedDateTime {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yy")
        return ZonedDateTime.parse(v, formatter.withZone(ZoneId.systemDefault()))
    }

    override fun marshal(v: ZonedDateTime): String {
        return v.format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yy"))
    }
}