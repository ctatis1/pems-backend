package com.ems.backend.entities;

import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrdenRequest {
    
    @NotBlank(message = "La moneda no puede estar vacía")
    @Pattern(regexp = "^(COP|USD)$", message = "La moneda debe ser COP o USD")
    private String moneda;

    @NotNull(message = "El ID del Cliente no puede ser nulo")
    @Min(value = 1, message = "El ID del Cliente debe ser un número positivo")
    private Long clienteId;

    @NotEmpty(message = "La lista de productos no puede estar vacía")
    @Valid
    private Map<
        @NotNull(message = "El ID del producto no puede ser nulo")
        @Min(value = 1, message = "El ID del Cliente debe ser un número positivo")
        Long,

        @NotNull(message = "La cantidad no puede ser nula")
        @Positive(message = "La cantidad debe ser mayor a 0")
        Integer
    > productos;
}
