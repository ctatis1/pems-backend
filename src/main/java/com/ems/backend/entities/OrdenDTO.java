package com.ems.backend.entities;

import java.util.List;

import lombok.Data;

@Data
public class OrdenDTO {
    private String clienteCorreo;
    private String moneda;
    private List<OrdenProductoDTO> productos;
}
