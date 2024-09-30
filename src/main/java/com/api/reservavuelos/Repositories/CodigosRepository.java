package com.api.reservavuelos.Repositories;

import com.api.reservavuelos.Models.Codigos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodigosRepository extends JpaRepository<Codigos, Long> {
  Optional<Codigos> findByCodigo(int codigo);
}
