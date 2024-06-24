package com.aremi.microservizio.dto.jaxb;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getDipendentiByIdSedeResponse", namespace = "http://example/infrastructure/sas-simulation-webservice")
@Data
public class GetDipendentiByIdSedeResponse {
    @XmlElement(name = "dipendenti", namespace = "http://example/infrastructure/sas-simulation-webservice")
    protected List<Dipendente> dipendenti;
    @XmlElement(name = "responseDetail", namespace = "http://example/infrastructure/sas-simulation-webservice")
    protected ResponseDetail responseDetail;

    public List<Dipendente> getDipendenti() {
        if (Objects.isNull(dipendenti)) {
            dipendenti = new ArrayList<>();
        }
        return this.dipendenti;
    }
}