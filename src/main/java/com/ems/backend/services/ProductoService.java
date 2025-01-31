package com.ems.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.backend.entities.Categoria;
import com.ems.backend.entities.Empresa;
import com.ems.backend.entities.Producto;
import com.ems.backend.entities.ProductoRequest;
import com.ems.backend.repositories.ProductoRepository;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getAll(){
        return productoRepository.findAll();
    }

    public Optional<Producto> getById(Long id){
        return productoRepository.findById(id);
    }

    public List<Producto> getByEmpresa(String empresaNit){
        return productoRepository.findByEmpresaNit(empresaNit);
    }

    public Producto save(Producto producto){
        return productoRepository.save(producto);
    }

    public Producto update(Long id, ProductoRequest productoRequest, Empresa empresa, Categoria categoria){
        Producto actualProducto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrada"));
        actualProducto.setCodigo(productoRequest.getCodigo());
        actualProducto.setCaracteristicas(productoRequest.getCaracteristicas());
        actualProducto.setCategorias(Set.of(categoria));
        actualProducto.setNombre(productoRequest.getNombre());
        actualProducto.setEmpresa(empresa);
        actualProducto.setPrecios(productoRequest.getPrecios());
        return productoRepository.save(actualProducto);
    }

    public void delete(Long id){
        productoRepository.deleteById(id);
    }
}
