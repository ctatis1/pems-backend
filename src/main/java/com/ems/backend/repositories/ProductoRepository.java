package com.ems.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.backend.entities.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{
    List<Producto> findByEmpresaNit(String empresaNit);
}
