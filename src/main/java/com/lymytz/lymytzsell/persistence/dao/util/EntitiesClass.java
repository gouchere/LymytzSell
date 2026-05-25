package com.lymytz.lymytzsell.persistence.dao.util;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EntitiesClass {
    @JacksonXmlProperty(localName = "entity-class")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<EntityClass> entities;
}
