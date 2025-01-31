package com.ems.backend.entities;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ProductoPDF {
    private Long id;
    private String codigo;
    private String nombre;
    private String caracteristicas;
    private int stock;
    private Map<String, Double> precios;
    private List<String> categoriasNombres;
    private EmpresaDTO empresa;
}
