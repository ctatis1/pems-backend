package com.ems.backend.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.backend.entities.Cliente;
import com.ems.backend.entities.Orden;
import com.ems.backend.entities.OrdenRequest;
import com.ems.backend.entities.OrdenResponse;
import com.ems.backend.entities.Producto;
import com.ems.backend.repositories.ClienteRepository;
import com.ems.backend.repositories.OrdenRepository;
import com.ems.backend.repositories.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
public class OrdenService {
    @Autowired
    private OrdenRepository ordenRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProductoRepository productoRepository;

    public List<Orden> getAll() {
        return ordenRepository.findAll();
    }

    public Optional<Orden> getById(Long id) {
        return ordenRepository.findById(id);
    }

    public List<Orden> getByClienteId(Long clienteId) {
        return ordenRepository.findByClienteId(clienteId);
    }

    public OrdenResponse updateOrden(Long id, OrdenRequest ordenDetails) {
        Orden orden = ordenRepository.findById(id).orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        orden.setMoneda(ordenDetails.getMoneda()); 
        Cliente cliente = clienteRepository.findById(ordenDetails.getClienteId()).orElseThrow(() -> new RuntimeException("Cliente no encontrada")); 
        orden.setCliente(cliente);

        Map<Producto, Integer> updatedProductos = new HashMap<>();
        double total = 0; 

        if (ordenDetails.getProductos() != null && !ordenDetails.getProductos().isEmpty()) {
            for (Map.Entry<Long, Integer> entry : ordenDetails.getProductos().entrySet()) {
                Long productoId = entry.getKey();  
                Integer cantidad = entry.getValue();
    
                Producto producto = productoRepository.findById(productoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    
                updatedProductos.put(producto, cantidad);
                total += producto.getPrecios().get(orden.getMoneda()) * cantidad;
            }
        }

        orden.setProductos(updatedProductos);  
        orden.setTotal(total); 

        ordenRepository.save(orden);

        OrdenResponse ordenResponse = new OrdenResponse(orden);

        return ordenResponse;
    }

    @Transactional
    public Orden save(String moneda, Long clienteId, Map<Long, Integer> productos) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Orden orden = new Orden();
        orden.setMoneda(moneda);
        orden.setCliente(cliente);

        Map<Producto, Integer> productosMap = new HashMap<>();
        double total = 0;

        for (Map.Entry<Long, Integer> entry : productos.entrySet()) {
            Producto producto = productoRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            if (producto.getStock() < entry.getValue()) {
                throw new RuntimeException("Stock insuficiente para producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - entry.getValue()); 
            productoRepository.save(producto);

            productosMap.put(producto, entry.getValue());
            total += producto.getPrecios().get(moneda) * entry.getValue();
        }

        orden.setProductos(productosMap);
        orden.setTotal(total);

        return ordenRepository.save(orden);
    }

    @Transactional
    public void delete(Long id) {
        Orden orden = ordenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // Restaurar el stock antes de eliminar
        orden.getProductos().forEach((producto, cantidad) -> {
            producto.setStock(producto.getStock() + cantidad);
            productoRepository.save(producto);
        });

        ordenRepository.delete(orden);
    }
}
