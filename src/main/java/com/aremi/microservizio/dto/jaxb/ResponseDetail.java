package com.aremi.microservizio.dto.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "genericResponse", propOrder = {
        "entitiesNumber",
        "httpCode",
        "description"
})
@Data
public class ResponseDetail {
    @XmlElement(name = "entitiesNumber", namespace = "http://example/infrastructure/sas-simulation-webservice")
    protected Integer entitiesNumber;
    @XmlElement(name = "httpCode", namespace = "http://example/infrastructure/sas-simulation-webservice")
    protected Integer httpCode;
    @XmlElement(name = "description", namespace = "http://example/infrastructure/sas-simulation-webservice")
    protected String description;
}