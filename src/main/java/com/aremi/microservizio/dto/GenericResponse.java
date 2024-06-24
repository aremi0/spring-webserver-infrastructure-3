package com.aremi.microservizio.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class GenericResponse<T> {
    private List<T> entities;
    private int entitiesNumber;
    private Integer httpCode;
    private String description;

    public List<T> getEntities() {
        if(Objects.isNull(entities)) {
            entities = new ArrayList<>();
        }
        return entities;
    }
}
