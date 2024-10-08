package com.api.reservavuelos.Repositories;

import com.api.reservavuelos.Models.Credenciales;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CredencialesRepository extends JpaRepository<Credenciales, Long> {

    @Query(
            value = "SELECT c.id_credencial, c.contraseña " +
                    "FROM credenciales c " +
                    "INNER JOIN usuarios u " +
                    "ON c.id_credencial = u.id_credencial " +
                    "WHERE u.email = :email ",
            nativeQuery = true
    )
    Credenciales getPasswordByEmail(@Param("email") String email);
}
