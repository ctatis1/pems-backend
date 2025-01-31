package com.ems.backend.entities;

import lombok.Data;

@Data
public class ProductoResponse {
    private Long id;
    private String nombre;
    private String codigo;
    private Integer stock;
    private Integer cantidad; // Cantidad de este producto en la orden

    public ProductoResponse(Producto producto, Integer cantidad) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.codigo = producto.getCodigo();
        this.stock = producto.getStock();
        this.cantidad = cantidad;
    }
}
