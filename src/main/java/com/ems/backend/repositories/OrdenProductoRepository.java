package com.ems.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ems.backend.entities.OrdenProducto;

@Repository
public interface OrdenProductoRepository extends JpaRepository<OrdenProducto, Long> {
    @Modifying
    @Query("DELETE FROM OrdenProducto op WHERE op.orden.id = :ordenId")
    void deleteByOrdenId(@Param("ordenId") Long ordenId);
}