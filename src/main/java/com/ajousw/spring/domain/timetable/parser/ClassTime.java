package com.ajousw.spring.domain.timetable.parser;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class ClassTime {
    @JacksonXmlProperty(isAttribute = true)
    private int day;

    @JacksonXmlProperty(isAttribute = true, localName = "starttime")
    private int startTime;

    @JacksonXmlProperty(isAttribute = true, localName = "endtime")
    private int endTime;

    @JacksonXmlProperty(isAttribute = true)
    private String place;
}
