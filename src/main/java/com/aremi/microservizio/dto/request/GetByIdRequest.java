package com.aremi.microservizio.dto.request;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "id"
})
@Data
public class GetByIdRequest {
    @XmlElement(namespace = "http://example/infrastructure/sas-simulation-webservice")
    protected Long id;
}
