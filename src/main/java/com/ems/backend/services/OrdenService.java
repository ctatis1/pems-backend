package com.ems.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.ems.backend.entities.Cliente;
import com.ems.backend.entities.Orden;
import com.ems.backend.entities.OrdenDTO;
import com.ems.backend.entities.OrdenProducto;
import com.ems.backend.entities.Producto;
import com.ems.backend.repositories.ClienteRepository;
import com.ems.backend.repositories.OrdenProductoRepository;
import com.ems.backend.repositories.OrdenRepository;
import com.ems.backend.repositories.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdenService {
    @Autowired
    private OrdenRepository ordenRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private OrdenProductoRepository ordenProductoRepository;

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

    public Orden save(OrdenDTO ordenDTO) {
        Cliente cliente = clienteRepository.findClienteByCorreo(ordenDTO.getClienteCorreo())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Orden orden = new Orden();
        orden.setCliente(cliente);
        orden.setMoneda(ordenDTO.getMoneda());

        Double precioTotal = 0.0;

        List<OrdenProducto> ordenProductos = ordenDTO.getProductos().stream()
                .map(dto -> {
                    Producto producto = productoRepository.findById(dto.getProductoId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                    return new OrdenProducto(orden, producto, dto.getCantidad());
                })
                .collect(Collectors.toList());
        for(OrdenProducto op: ordenProductos){
            Producto prod = op.getProducto();
            Integer cantidad = op.getCantidad();
            if(prod.getStock() < cantidad) throw new RuntimeException("No hay suficientes unidades para esta orden");
            prod.setStock( prod.getStock() - cantidad);
            if(orden.getMoneda().equals("COP")) precioTotal += prod.getPrecios().get("COP") * cantidad;
            if(orden.getMoneda().equals("USD")) precioTotal += prod.getPrecios().get("USD") * cantidad;
        }
        orden.setTotal(precioTotal);
        orden.setOrdenProductos(ordenProductos);

        return ordenRepository.save(orden);
    }

    public void delete(Long id) {
        try {
            Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

            orden.getOrdenProductos().forEach(ordenProducto -> {
                Producto producto = ordenProducto.getProducto();
                producto.setStock(producto.getStock() + ordenProducto.getCantidad());
                productoRepository.save(producto);
            });

            orden.getOrdenProductos().clear();
            ordenRepository.save(orden);

            ordenRepository.delete(orden);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new RuntimeException("La orden fue modificada por otra transacción, por favor intente de nuevo.");
        }
    }
}
