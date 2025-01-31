package com.ems.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.backend.entities.Empresa;
import com.ems.backend.repositories.EmpresaRepository;

@Service
public class EmpresaService {
    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Empresa> getAll(){
        return empresaRepository.findAll();
    }

    public Optional<Empresa> getByNit(String nit){
        return empresaRepository.findById(nit);
    }
    public Empresa updateEmpresa(String nit, Empresa empresa){
        Empresa actualEmpresa = empresaRepository.findById(nit).orElseThrow( () -> new RuntimeException("Orden no encontrada") );
        actualEmpresa.setNombre(empresa.getNombre());
        actualEmpresa.setDireccion(empresa.getDireccion());
        actualEmpresa.setTelefono(empresa.getTelefono());
        return empresaRepository.save(actualEmpresa);
    }

    public Empresa save(Empresa empresa){
        return empresaRepository.save(empresa);
    }

    public void delete(String nit){
        empresaRepository.deleteById(nit);
    }
}
