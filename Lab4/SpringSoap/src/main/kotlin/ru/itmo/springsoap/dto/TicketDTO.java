package ru.itmo.springsoap.dto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.itmo.springsoap.Model.Coordinates;
import ru.itmo.springsoap.Model.Event;
import ru.itmo.springsoap.Model.TicketType;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;

@XmlRootElement(name = "ticket")
public class TicketDTO {
    @XmlAttribute
    private long id;
    @XmlElement
    private String creationDate;
    @XmlElement
    private String name;

    @XmlElement

    private CoordinatesDTO coordinates;
    @XmlElement

    private Long price;
    @XmlElement

    private Integer discount;
    @XmlElement

    private String comment;
    @XmlElement

    private TicketType type;

    @XmlElement
    private EventDTO event;
}
