package com.ajousw.spring.domain.timetable.parser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubjectParse {
    @JacksonXmlProperty(isAttribute = true)
    private int id;

    @JacksonXmlProperty(localName = "internal")
    private Internal internal;

    @JacksonXmlProperty(localName = "name")
    private Name name;

    @JacksonXmlProperty(localName = "professor")
    private Professor professor;

    @JacksonXmlProperty(localName = "time")
    private List<ClassTime> classTimes;

    public String getInternal() {
        return internal.getValue();
    }

    public String getName() {
        return name.getValue();
    }

    public String getProfessor() {
        return professor.getValue();
    }

    @Data
    class Internal {
        @JacksonXmlProperty(isAttribute = true)
        private String value;
    }

    @Data
    class Name {
        @JacksonXmlProperty(isAttribute = true)
        private String value;
    }

    @Data
    class Professor {
        @JacksonXmlProperty(isAttribute = true)
        private String value;
    }
}


