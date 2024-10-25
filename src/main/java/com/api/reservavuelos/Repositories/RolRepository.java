package com.api.reservavuelos.Repositories;

import com.api.reservavuelos.Models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RolRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByNombre(String nombre);
}
