package com.api.reservavuelos.Services;

import com.api.reservavuelos.DTO.Request.LoginDTO;
import com.api.reservavuelos.DTO.Request.RegisterDTO;
import com.api.reservavuelos.Exceptions.UserAlreadyRegisterException;
import com.api.reservavuelos.Repositories.AuthRepository;
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
    private AuthRepository authRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private RegisterDTO registerDTO;
    private LoginDTO loginDTO;

    @BeforeEach
    public void setUp() {
        registerDTO = new RegisterDTO();
        registerDTO.setPrimer_nombre("Carlos");
        registerDTO.setSegundo_nombre("Andres");
        registerDTO.setPrimer_apellido("Salas");
        registerDTO.setSegundo_apellido("Correa");
        registerDTO.setEmail("carlosasalas321@gmail.com");
        registerDTO.setTelefono("3106931114");
        registerDTO.setContraseña("Carlossalas31082005");

        loginDTO = new LoginDTO();
        loginDTO.setEmail("carlosasalas321@gmail.com");
        loginDTO.setContraseña("Carlossalas31082005");
    }

    @Test
    public void registrarUsuario_UserAlreadyExists(){
        when(authRepository.existsByEmail(registerDTO.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyRegisterException.class, () -> authService.registrarUsuario(registerDTO));
  }
}
