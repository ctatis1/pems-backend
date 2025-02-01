package com.ems.backend.entities;

import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductoRequest {
    @NotBlank(message = "El codigo es obligatorio")
    @NotNull(message = "El codigo no puede ser nulo")
    @Size(min = 5, max = 50, message = "El codigo debe tener entre 5 y 50 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "El codigo debe contener solo números")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    private String caracteristicas;

    @NotEmpty(message = "Los precios no pueden estar vacíos")
    @Valid
    private Map<
        @NotBlank(message = "La moneda no puede estar vacía")
        @Pattern(regexp = "^(COP|USD)$", message = "Solo se permiten monedas COP o USD")
        String, 
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        Double> precios;
        
    @NotBlank(message = "El NIT de la Empresa es obligatorio")
    @NotNull(message = "El NIT de la Empresa no puede ser nulo")
    @Pattern(regexp = "^[0-9]+$", message = "El NIT de la Empresa debe contener solo números")
    private String empresaNit;

    @NotBlank(message = "El ID de la Categoria es obligatorio")
    @NotNull(message = "El ID de la Categoria no puede ser nulo")
    @Pattern(regexp = "^[0-9]+$", message = "El ID de la Categoria debe contener solo números")
    private String categoriaId;

    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 1, message = "El ID del Cliente debe ser un número positivo")
    private Integer stock;
}
