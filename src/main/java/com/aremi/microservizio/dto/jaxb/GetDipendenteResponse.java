package com.aremi.microservizio.dto.jaxb;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "dipendente",
        "responseDetail"
})
@XmlRootElement(name = "getDipendenteResponse", namespace = "http://example/infrastructure/sas-simulation-webservice")
@Data
public class GetDipendenteResponse {
    @XmlElement(name = "dipendente", namespace = "http://example/infrastructure/sas-simulation-webservice")
    protected Dipendente dipendente;
    @XmlElement(name = "responseDetail", namespace = "http://example/infrastructure/sas-simulation-webservice")
    protected ResponseDetail responseDetail;
}
