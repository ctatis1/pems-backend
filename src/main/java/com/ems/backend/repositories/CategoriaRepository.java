package com.ems.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.backend.entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    
}
