package com.ems.backend.entities;

import java.util.Map;

import lombok.Data;

@Data
public class OrdenRequest {
    private String moneda;
    private Long clienteId;
    private Map<Long, Integer> productos;
}
