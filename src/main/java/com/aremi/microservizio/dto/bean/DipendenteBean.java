package com.aremi.microservizio.dto.bean;

import lombok.Data;

import javax.xml.datatype.XMLGregorianCalendar;

@Data
public class DipendenteBean {
    private String nome;
    private String cognome;
    private XMLGregorianCalendar dataDiNascita;
    private String email;
    private String ruolo;
    private String sedeDiLavoro;
}
