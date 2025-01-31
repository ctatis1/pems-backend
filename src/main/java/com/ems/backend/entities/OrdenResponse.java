package com.ems.backend.entities;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class OrdenResponse {
    private Long id;
    private String moneda;
    private Double total;
    private String clienteCorreo;
    private List<ProductoResponse> productos; // Lista de productos con info básica

    public OrdenResponse(Orden orden) {
        this.id = orden.getId();
        this.moneda = orden.getMoneda();
        this.total = orden.getTotal();
        this.clienteCorreo = orden.getCliente().getCorreo();
        this.productos = orden.getProductos().entrySet().stream()
                .map(entry -> new ProductoResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
