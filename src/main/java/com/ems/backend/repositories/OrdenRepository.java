package com.ems.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.backend.entities.Orden;

public interface OrdenRepository extends JpaRepository<Orden, Long>{
    List<Orden> findByClienteId(Long clienteId);
}
