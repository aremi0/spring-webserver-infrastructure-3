package com.aremi.microservizio.dto.bean;

import java.sql.Timestamp;

public interface DipendenteBean {
    String getNome();
    String getCognome();
    Timestamp getData_Di_Nascita();
    String getEmail();
    String getRuolo();
    String sede_Di_Lavoro();
}
