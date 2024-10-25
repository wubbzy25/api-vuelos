package com.api.reservavuelos.Services;

import com.api.reservavuelos.DTO.Request.LoginRequestDTO;
import com.api.reservavuelos.DTO.Request.RegisterRequestDTO;
import com.api.reservavuelos.Exceptions.UserAlreadyRegisterException;
import com.api.reservavuelos.Repositories.UsuarioRepository;
import com.api.reservavuelos.Repositories.CredencialesRepository;
import com.api.reservavuelos.Repositories.RolRepository;
import com.api.reservavuelos.Security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {


    @Mock
    private RolRepository rolRepository;
    @Mock
    private CredencialesRepository credencialesRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private RegisterRequestDTO registerRequestDTO;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    public void setUp() {
        registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setPrimer_nombre("Carlos");
        registerRequestDTO.setSegundo_nombre("Andres");
        registerRequestDTO.setPrimer_apellido("Salas");
        registerRequestDTO.setSegundo_apellido("Correa");
        registerRequestDTO.setEmail("carlosasalas321@gmail.com");
        registerRequestDTO.setTelefono("3106931114");
        registerRequestDTO.setContraseña("Carlossalas31082005");

        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("carlosasalas321@gmail.com");
        loginRequestDTO.setContraseña("Carlossalas31082005");
    }

    @Test
    public void registrarUsuario_UserAlreadyExists(){
        when(usuarioRepository.existsByEmail(registerRequestDTO.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyRegisterException.class, () -> authService.registrarUsuario(registerRequestDTO));
  }
}
