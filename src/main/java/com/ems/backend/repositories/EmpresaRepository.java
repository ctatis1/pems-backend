package com.ems.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.backend.entities.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, String>{
    
}
