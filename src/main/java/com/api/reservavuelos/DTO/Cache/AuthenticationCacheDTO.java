package com.api.reservavuelos.DTO.Cache;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthenticationCacheDTO implements Serializable {
 private String email;
 private String contraseña;
}
