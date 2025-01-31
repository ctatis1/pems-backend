package com.ems.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.backend.entities.Categoria;
import com.ems.backend.repositories.CategoriaRepository;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getAll() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> getById(Long id) {
        return categoriaRepository.findById(id);
    }

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }
    public Categoria update(Long id, Categoria categoria) {
        Categoria actualCategoria = categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        actualCategoria.setNombre(categoria.getNombre());
        return categoriaRepository.save(actualCategoria);
    }

    public void delete(Long id) {
        categoriaRepository.deleteById(id);
    }
}
