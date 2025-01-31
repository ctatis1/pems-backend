package com.ems.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.backend.entities.Cliente;
import com.ems.backend.repositories.ClienteRepository;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> getAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> getById(Long id) {
        return clienteRepository.findById(id);
    }
    public Optional<Cliente> getByCorreo(String correo) {
        return clienteRepository.findClienteByCorreo(correo);
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    public Cliente update(Long id, Cliente cliente) {
        Cliente actualCliente = clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        actualCliente.setCorreo(cliente.getCorreo());
        actualCliente.setNombre(cliente.getNombre());
        return clienteRepository.save(actualCliente);
    }

    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }
}
