package com.ems.backend.entities;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(schema = "ems")
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String moneda;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnore
    private Cliente cliente;

    @JsonProperty("cliente")
    public Map<String, String> getClienteInfo() {
        Map<String, String> clienteInfo = new HashMap<>();
        if (cliente != null) {
            clienteInfo.put("correo", cliente.getCorreo());
            clienteInfo.put("nombre", cliente.getNombre());
        }
        return clienteInfo;
    }

    @ElementCollection
    @CollectionTable(name = "orden_producto", joinColumns = @JoinColumn(name = "orden_id"))
    @MapKeyJoinColumn(name = "producto_id")
    @Column(name = "cantidad")
    @JsonIgnore
    private Map<Producto, Integer> productos = new HashMap<>();

    private Double total;
}
