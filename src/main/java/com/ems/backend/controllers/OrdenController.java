package com.ems.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.backend.entities.Orden;
import com.ems.backend.entities.OrdenRequest;
import com.ems.backend.entities.OrdenResponse;
import com.ems.backend.services.OrdenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orden")
public class OrdenController {
    @Autowired
    private OrdenService ordenService;

    @GetMapping
    public ResponseEntity<List<OrdenResponse>> getAllOrdenes() {
        List<Orden> ordenes = ordenService.getAll();
        List<OrdenResponse> response = ordenes.stream()
                .map(OrdenResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponse> getOrdenById(@PathVariable Long id) {
        Optional<Orden> orden = ordenService.getById(id);
        if (orden.isPresent()) {
            OrdenResponse ordenResponse = new OrdenResponse(orden.get());
            return ResponseEntity.ok(ordenResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrden(@Valid @RequestBody OrdenRequest ordenRequest, BindingResult result) {
        if(result.hasErrors()) return validation(result);
        ordenService.save(ordenRequest.getMoneda(), ordenRequest.getClienteId(), ordenRequest.getProductos());
        return ResponseEntity.status(HttpStatus.CREATED).body("Orden creada exitosamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrden(@PathVariable Long id, @Valid @RequestBody OrdenRequest ordenDetails, BindingResult result) {
        if(result.hasErrors()) return validation(result);
        ordenService.updateOrden(id, ordenDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body("Orden actualizada exitosamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrden(@PathVariable Long id) {
        ordenService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
