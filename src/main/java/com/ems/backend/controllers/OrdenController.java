package com.ems.backend.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.backend.entities.Orden;
import com.ems.backend.entities.OrdenDTO;
import com.ems.backend.services.OrdenService;

@RestController
@RequestMapping("/orden")
public class OrdenController {
    @Autowired
    private OrdenService ordenService;

    @GetMapping
    public List<Orden> getAllOrdenes() {
        return ordenService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> getOrdenById(@PathVariable Long id) {
        Optional<Orden> orden = ordenService.getById(id);
        if (orden.isPresent()) {
            return ResponseEntity.ok(orden.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrden(@RequestBody OrdenDTO ordenDTO) {
        ordenService.save(ordenDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Orden creada exitosamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orden> updateOrden(@PathVariable Long id, @RequestBody Orden ordenDetails) {
        try {
            Orden updatedOrden = ordenService.updateOrden(id, ordenDetails);
            return ResponseEntity.ok(updatedOrden);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrden(@PathVariable Long id) {
        ordenService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
