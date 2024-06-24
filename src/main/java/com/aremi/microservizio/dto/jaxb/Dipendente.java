package com.aremi.microservizio.dto.jaxb;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dipendente", propOrder = {
        "idDipendente",
        "nome",
        "cognome",
        "dataDiNascita"
})
@Data
public class Dipendente {
    @XmlElement(name = "id_dipendente", namespace = "http://example/infrastructure/sas-simulation-webservice")
    private Long idDipendente;
    @XmlElement(name = "nome", namespace = "http://example/infrastructure/sas-simulation-webservice")
    private String nome;
    @XmlElement(name = "cognome", namespace = "http://example/infrastructure/sas-simulation-webservice")
    private String cognome;
    @XmlElement(name = "data_di_nascita", namespace = "http://example/infrastructure/sas-simulation-webservice")
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar dataDiNascita;
}
