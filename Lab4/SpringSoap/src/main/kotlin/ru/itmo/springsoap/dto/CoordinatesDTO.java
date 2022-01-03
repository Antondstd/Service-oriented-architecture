package ru.itmo.springsoap.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "coordinates")
public class CoordinatesDTO {
    @XmlAttribute
    private long id;
    @XmlElement
    private Integer x;
    @XmlElement
    private Long y;
}
