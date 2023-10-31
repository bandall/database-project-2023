package com.ajousw.spring.domain.timetable.parser;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "table")
public class TableParse {
    @JacksonXmlProperty(isAttribute = true)
    private String year;

    @JacksonXmlProperty(isAttribute = true)
    private String semester;

    @JacksonXmlProperty(isAttribute = true)
    private String status;

    @JacksonXmlProperty(isAttribute = true)
    private String identifier;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "subject")
    private List<SubjectParse> subjects;
}
