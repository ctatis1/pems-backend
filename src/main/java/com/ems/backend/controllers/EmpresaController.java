package com.ems.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.backend.entities.Empresa;
import com.ems.backend.services.EmpresaService;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {
    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public List<Empresa> getAllEmpresas() {
        return empresaService.getAll();
    }

    @GetMapping("/{nit}")
    public ResponseEntity<Empresa> getEmpresa(@PathVariable String nit) {
        Optional<Empresa> empresa = empresaService.getByNit(nit);
        return empresa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createEmpresa(@RequestBody Empresa empresa) {
        empresaService.save(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).body("Empresa creada exitosamente");
    }

    @DeleteMapping("/{nit}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable String nit) {
        empresaService.delete(nit);
        return ResponseEntity.noContent().build();
    }
}
