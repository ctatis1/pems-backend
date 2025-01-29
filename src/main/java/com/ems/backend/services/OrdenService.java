package com.ems.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.backend.entities.Orden;
import com.ems.backend.repositories.OrdenRepository;

@Service
public class OrdenService {
    @Autowired
    private OrdenRepository ordenRepository;

    public List<Orden> getAll() {
        return ordenRepository.findAll();
    }

    public Optional<Orden> getById(Long id) {
        return ordenRepository.findById(id);
    }

    public List<Orden> getByClienteId(Long clienteId) {
        return ordenRepository.findByClienteId(clienteId);
    }

    public Orden updateOrden(Long id, Orden ordenDetails) {
        Orden orden = ordenRepository.findById(id).orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        orden.setTotal(ordenDetails.getTotal());
        orden.setProductos(ordenDetails.getProductos());
        return ordenRepository.save(orden);
    }

    public Orden save(Orden orden) {
        return ordenRepository.save(orden);
    }

    public void delete(Long id) {
        ordenRepository.deleteById(id);
    }
}
