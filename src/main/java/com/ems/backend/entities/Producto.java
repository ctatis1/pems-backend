package com.ems.backend.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(schema = "ems")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String nombre;
    private String caracteristicas;
    private Integer stock;

    @ElementCollection
    private Map<String, Double> precios;

    @ManyToOne
    @JoinColumn(name = "empresa_nit", nullable = false)
    @JsonIgnore
    private Empresa empresa;

    @ManyToMany
    @JoinTable(
        name = "producto_categoria",
        joinColumns = @JoinColumn( name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @JsonIgnore
    private Set<Categoria> categorias;

    @JsonProperty("empresa")
    public Map<String, String> getEmpresaInfo() {
        Map<String, String> empresaInfo = new HashMap<>();
        if (empresa != null) {
            empresaInfo.put("nit", empresa.getNit());
            empresaInfo.put("nombre", empresa.getNombre());
        }
        return empresaInfo;
    }

    @JsonProperty("categoriasNombres")
    public Set<String> getCategoriasNombres() {
        return categorias.stream()
                        .map(Categoria::getNombre)
                        .collect(Collectors.toSet());
    }
}
