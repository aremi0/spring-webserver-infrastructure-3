package com.aremi.microservizio.dto.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getUtenteByEmailRequest", namespace = "http://example/infrastructure/sas-simulation-webservice")
@Data
public class GetUtenteByEmailRequest {
    @XmlElement(name = "emailUtente", namespace = "http://example/infrastructure/sas-simulation-webservice")
    protected String emailUtente;
}
