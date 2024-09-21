package com.api.reservavuelos.DTO;

import lombok.Data;

@Data
public class usuarioDTO {
  private Long id;
  private String primer_nombre;
  private String segundo_nombre;
  private String primer_apellido;
  private String segundo_apellido;
  private String email;
  private int telefono;
  private String contraseña;
}
