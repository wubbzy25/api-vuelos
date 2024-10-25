package com.api.reservavuelos.Repositories;

import com.api.reservavuelos.Models.Metodos_pagos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MetodosPagosRepository extends JpaRepository<Metodos_pagos, Long> {

          @Query(
                  value= "SELECT CASE WHEN COUNT(mp) > 0 THEN TRUE ELSE FALSE END " +
                  "FROM Metodos_pagos mp " +
                  "WHERE mp.id_usuario = :usuarioId",
                   nativeQuery = true
          )
          Boolean UsuarioTieneMetodoPago(Long usuarioId);
        @Query(
            value = "SELECT mp.id_metodo_pago, mp.nombre_titular, mp.numero, mp.fecha_vencimiento, mp.id_usuario FROM metodos_pagos mp " +
                    "INNER JOIN usuarios u ON " +
                    "mp.id_usuario = u.id "  +
                    "WHERE mp.id_usuario = :usuarioId",
                nativeQuery = true
          )
          List<Metodos_pagos> obtenermetodosdepago(Long usuarioId);
         @Query(
                 value = "SELECT * FROM metodos_pagos mp " +
                         "INNER JOIN usuarios u " +
                         "ON mp.id_usuario = u.id " +
                         "WHERE mp.id_metodo_pago = :id_metodoPago " +
                         "AND  mp.id_usuario = :usuarioId",
                  nativeQuery = true
         )
          Metodos_pagos obtenermetododepago(Long usuarioId, Long id_metodoPago);
}


