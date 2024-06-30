package com.aremi.microservizio.dto.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "utente", propOrder = {
        "email",
        "password",
        "autorita",
})
@Data
public class Utente {
    @XmlElement(name = "email", namespace = "http://example/infrastructure/sas-simulation-webservice")
    private String email;
    @XmlElement(name = "password", namespace = "http://example/infrastructure/sas-simulation-webservice")
    private String password;
    @XmlElement(name = "autorita", namespace = "http://example/infrastructure/sas-simulation-webservice")
    private String autorita;
}
