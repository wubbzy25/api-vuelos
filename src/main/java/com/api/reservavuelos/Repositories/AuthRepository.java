package com.api.reservavuelos.Repositories;

import com.api.reservavuelos.Models.Usuarios;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.util.annotation.NonNullApi;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Usuarios, Long> {
    Optional<Usuarios> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query(
            value = "SELECT contraseña FROM credenciales " +
                    "INNER JOIN usuarios " +
                    "ON credenciales.id_credencial = usuarios.id_credencial " +
                    "WHERE usuarios.email = :email",
            nativeQuery = true
    )
    Optional<String> findPasswordByEmail(@Param("email") String email);
    Usuarios getById(Long id);
}

