package com.api.reservavuelos.Repositories;

import com.api.reservavuelos.Models.Vuelos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface VuelosRepository extends JpaRepository<Vuelos, Long> {
    Optional<Vuelos> getVuelosByNumeroVuelo(int numeroVuelo);
}
