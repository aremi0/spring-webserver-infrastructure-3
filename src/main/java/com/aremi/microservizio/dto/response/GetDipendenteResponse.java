package com.aremi.microservizio.dto.response;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getDipendenteResponse", namespace = "http://example/infrastructure/sas-simulation-webservice")
@Data
public class GetDipendenteResponse {
    @XmlElement(name = "dipendente", namespace = "http://example/infrastructure/sas-simulation-webservice")
    protected Dipendente dipendente;
    @XmlElement(name = "responseDetail", namespace = "http://example/infrastructure/sas-simulation-webservice")
    protected ResponseDetail responseDetail;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class Dipendente {
        @XmlElement(name = "id_dipendente", namespace = "http://example/infrastructure/sas-simulation-webservice")
        protected Long idDipendente;
        @XmlElement(name = "nome", namespace = "http://example/infrastructure/sas-simulation-webservice")
        protected String nome;
        @XmlElement(name = "cognome", namespace = "http://example/infrastructure/sas-simulation-webservice")
        protected String cognome;
        @XmlElement(name = "data_di_nascita", namespace = "http://example/infrastructure/sas-simulation-webservice")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataDiNascita;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class ResponseDetail {
        @XmlElement(name = "entitiesNumber", namespace = "http://example/infrastructure/sas-simulation-webservice")
        protected Integer entitiesNumber;
        @XmlElement(name = "httpCode", namespace = "http://example/infrastructure/sas-simulation-webservice")
        protected Integer httpCode;
        @XmlElement(name = "description", namespace = "http://example/infrastructure/sas-simulation-webservice")
        protected String description;
    }
}
