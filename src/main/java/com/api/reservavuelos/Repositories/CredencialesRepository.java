package com.api.reservavuelos.Repositories;

import com.api.reservavuelos.Models.Credenciales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredencialesRepository extends JpaRepository<Credenciales, Long> {
}
