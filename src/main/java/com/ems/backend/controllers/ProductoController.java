package com.ems.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

import com.ems.backend.entities.Categoria;
import com.ems.backend.entities.Empresa;
import com.ems.backend.entities.Producto;
import com.ems.backend.entities.ProductoRequest;
import com.ems.backend.services.CategoriaService;
import com.ems.backend.services.EmpresaService;
import com.ems.backend.services.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/producto")
public class ProductoController {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        Optional<Producto> producto = productoService.getById(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/empresa/{empresaNit}")
    public List<Producto> listarPorEmpresa(@PathVariable String empresaNit) {
        return productoService.getByEmpresa(empresaNit);
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody ProductoRequest productoRequest, BindingResult result) {
        if(result.hasErrors()) return validation(result);
        Empresa empresa = empresaService.getByNit(productoRequest.getEmpresaNit()).orElseThrow( () -> new RuntimeException("Empresa no encontrada") );
        Categoria categoria = categoriaService.getById(Long.valueOf(productoRequest.getCategoriaId())).orElseThrow( () -> new RuntimeException("Categoria no encontrada") );

        Producto producto = new Producto();
        producto.setCodigo(productoRequest.getCodigo());
        producto.setCaracteristicas(productoRequest.getCaracteristicas());
        producto.setCategorias(Set.of(categoria));
        producto.setNombre(productoRequest.getNombre());
        producto.setEmpresa(empresa);
        producto.setStock(productoRequest.getStock());
        producto.setPrecios(productoRequest.getPrecios());

        productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto creado exitosamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequest productoRequest, BindingResult result) {
        if(result.hasErrors()) return validation(result);
        Empresa empresa = empresaService.getByNit(productoRequest.getEmpresaNit()).orElseThrow( () -> new RuntimeException("Empresa no encontrada") );
        Categoria categoria = categoriaService.getById(Long.valueOf(productoRequest.getCategoriaId())).orElseThrow( () -> new RuntimeException("Categoria no encontrada") );
        productoService.update(id, productoRequest, empresa, categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto actualizado exitosamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
