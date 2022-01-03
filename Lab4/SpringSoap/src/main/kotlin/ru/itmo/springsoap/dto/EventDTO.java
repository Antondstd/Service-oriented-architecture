package ru.itmo.springsoap.dto;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement(name = "event")
public class EventDTO {
    @XmlAttribute
    private long id;
    @XmlElement
    private String name;
    @XmlElement
    private String date;
    @XmlElement
    private Integer minAge;
}
