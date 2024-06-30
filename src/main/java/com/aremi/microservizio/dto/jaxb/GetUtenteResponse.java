package com.aremi.microservizio.dto.jaxb;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "utente",
        "responseDetail"
})
@XmlRootElement(name = "getUtenteResponse", namespace = "http://example/infrastructure/sas-simulation-webservice")
@Data
public class GetUtenteResponse {
    @XmlElement(name = "utente", namespace = "http://example/infrastructure/sas-simulation-webservice")
    protected Utente utente;
    @XmlElement(name = "responseDetail", namespace = "http://example/infrastructure/sas-simulation-webservice")
    protected ResponseDetail responseDetail;
}
