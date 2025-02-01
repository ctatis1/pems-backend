package com.ems.backend.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(schema = "ems")
public class Empresa {
    @Id
    @NotBlank(message = "El NIT es obligatorio")
    @NotNull(message = "El NIT no puede ser nulo")
    @Size(min = 9, max = 11, message = "El NIT debe tener entre 9 y 10 caracteres")
    private String nit;

    @NotBlank(message = "El nombre es obligatorio")
    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "La direccion es obligatorio")
    @NotNull(message = "La direccion no puede ser nulo")
    @Size(min = 15, max = 50, message = "La direccion debe tener entre 15 y 50 caracteres")
    private String direccion;

    @NotBlank(message = "El telefono es obligatorio")
    @NotNull(message = "El telefono no puede ser nulo")
    @Size(min = 8, max = 50, message = "El telefono debe tener entre 8 y 50 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "El telefono debe contener solo números")
    private String telefono;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productos;
    
}
