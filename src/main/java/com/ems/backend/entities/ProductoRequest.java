package com.ems.backend.entities;

import java.util.Map;

import lombok.Data;

@Data
public class ProductoRequest {
    private String codigo;
    private String nombre;
    private String caracteristicas;
    private Map<String, Double> precios;
    private String empresaNit;
    private String categoriaId;
    private Integer stock;
}
