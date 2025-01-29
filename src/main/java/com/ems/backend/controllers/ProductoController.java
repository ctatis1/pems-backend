package com.ems.backend.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import com.ems.backend.entities.Categoria;
import com.ems.backend.entities.Empresa;
import com.ems.backend.entities.Producto;
import com.ems.backend.entities.ProductoRequest;
import com.ems.backend.services.CategoriaService;
import com.ems.backend.services.EmpresaService;
import com.ems.backend.services.ProductoService;

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
    public ResponseEntity<?> crearProducto(@RequestBody ProductoRequest productoRequest) {
        Empresa empresa = empresaService.getByNit(productoRequest.getEmpresaNit()).orElse(null);
        Categoria categoria = categoriaService.getById(Long.valueOf(productoRequest.getCategoriaId())).orElse(null);

        Producto producto = new Producto();
        producto.setCodigo(productoRequest.getCodigo());
        producto.setCaracteristicas(productoRequest.getCaracteristicas());
        producto.setCategorias(Set.of(categoria));
        producto.setNombre(productoRequest.getNombre());
        producto.setEmpresa(empresa);

        productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto creado exitosamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
