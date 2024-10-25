package com.api.reservavuelos.Repositories;

import com.api.reservavuelos.Models.TwoFactorAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TwoFactorAuthRepository extends JpaRepository<TwoFactorAuth, Long> {
    @Query(
            value = "SELECT * FROM two_factor_auth WHERE id_usuario = :id_usuario",
            nativeQuery = true
    )
    Optional<TwoFactorAuth> findByid_usuario(Long id_usuario);
}
    