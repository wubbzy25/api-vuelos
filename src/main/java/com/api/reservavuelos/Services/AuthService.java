package com.api.reservavuelos.Services;
import com.api.reservavuelos.DTO.ForgotPasswordDTO;
import com.api.reservavuelos.DTO.LoginDTO;
import com.api.reservavuelos.DTO.RegisterDTO;
import com.api.reservavuelos.Exceptions.UserAlreadyRegisterException;
import com.api.reservavuelos.Exceptions.UserNotFoundException;
import com.api.reservavuelos.Models.Codigos;
import com.api.reservavuelos.Models.Credenciales;
import com.api.reservavuelos.Models.Roles;
import com.api.reservavuelos.Models.Usuarios;
import com.api.reservavuelos.Repositories.AuthRepository;
import com.api.reservavuelos.Repositories.CodigosRepository;
import com.api.reservavuelos.Repositories.CredencialesRepository;
import com.api.reservavuelos.Repositories.RolRepository;
import com.api.reservavuelos.Security.JwtTokenProvider;
import com.api.reservavuelos.Utils.GenerateCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private CodigosRepository codigosRepository;
    @Autowired
    private CredencialesRepository credencialesRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GenerateCodes generateCodes;




    public void registrarUsuario(RegisterDTO registerDTO){
       if (authRepository.existsByEmail(registerDTO.getEmail())){
           throw new UserAlreadyRegisterException();
       }
       Usuarios usuario = new Usuarios();
       usuario.setPrimer_nombre(registerDTO.getPrimer_nombre());
       usuario.setSegundo_nombre(registerDTO.getSegundo_nombre());
       usuario.setPrimer_apellido(registerDTO.getPrimer_apellido());
       usuario.setSegundo_apellido(registerDTO.getSegundo_apellido());
       usuario.setEmail(registerDTO.getEmail());
       usuario.setTelefono(registerDTO.getTelefono());
       Credenciales credencial = new Credenciales();
       credencial.setContraseña(passwordEncoder.encode(registerDTO.getContraseña()));
       credencialesRepository.save(credencial);
       usuario.setCredenciales(credencial);
       Roles roles = rolRepository.findByNombre("usuario").orElseThrow(() -> new NoSuchElementException("No se encontro el rol usuarios"));
       usuario.setRoles(Collections.singletonList(roles));
       authRepository.save(usuario);
    }

    public String login(LoginDTO dtoLogin){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dtoLogin.getEmail(), dtoLogin.getContraseña()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    public Void OlvidarContraseña(ForgotPasswordDTO forgotPasswordDTO){
        authRepository.findByEmail(forgotPasswordDTO.getEmail()).orElseThrow(UserNotFoundException::new);
        int code = generateCodes.code();
        Codigos codigo = new Codigos();
        codigo.setCodigo(code);
        codigo.setCreado(LocalDateTime.now());
        codigosRepository.save(codigo);
        return null;
    }


}
