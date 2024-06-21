package com.aremi.microservizio.dto;

import jakarta.xml.bind.annotation.XmlRegistry;
import lombok.NoArgsConstructor;

/***
 * Questa classe e' strettamente necessaria se non si utilizza il plugin Maven "xjc" per l'autogenerazione
 * delle classi POJO per Request e Response come ad esempio ho fatto nel progetto "simulatore-sas".
 * In questo progetto ho scelto di creare manualmente le classi POJO per esercizio, perciò è necessario includere
 * questa classe ulteriore, oltre le POJO, per permettere a JAXB di mappare l'XML all'interno delle classi che devono essere
 * inserite man qui dentro.
 */

@XmlRegistry
@NoArgsConstructor
public class ObjectFactory {

    public GetByIdRequest createGetByIdRequest() {
        return new GetByIdRequest();
    }

    public GetDipendenteResponse createGetDipendenteResponse() {
        return new GetDipendenteResponse();
    }

    // TODO: Aggiungi altri metodi create per altre classi se necessario
}