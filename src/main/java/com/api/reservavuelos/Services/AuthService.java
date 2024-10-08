package com.api.reservavuelos.Services;
import com.api.reservavuelos.DTO.Request.*;
import com.api.reservavuelos.Exceptions.CodeNotFoundException;
import com.api.reservavuelos.Exceptions.UserAlreadyRegisterException;
import com.api.reservavuelos.Exceptions.UserNotFoundException;
import com.api.reservavuelos.Models.Credenciales;
import com.api.reservavuelos.Models.Roles;
import com.api.reservavuelos.Models.Usuarios;
import com.api.reservavuelos.Repositories.AuthRepository;
import com.api.reservavuelos.Repositories.CredencialesRepository;
import com.api.reservavuelos.Repositories.RolRepository;
import com.api.reservavuelos.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final RolRepository rolRepository;
    private final CredencialesRepository credencialesRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final ResetPasswordService resetPasswordService;

    @Autowired
    public AuthService(AuthRepository authRepository,
                       RolRepository rolRepository,
                       CredencialesRepository credencialesRepository,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       EmailSenderService emailSenderService,
                       ResetPasswordService resetPasswordService) {
        this.authRepository = authRepository;
        this.rolRepository = rolRepository;
        this.credencialesRepository = credencialesRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.emailSenderService = emailSenderService;
        this.resetPasswordService = resetPasswordService;
    }


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
        return jwtTokenProvider.generateToken(authentication);
    }

    public void OlvidarContraseña(ForgotPasswordDTO forgotPasswordDTO){
        authRepository.findByEmail(forgotPasswordDTO.getEmail()).orElseThrow(UserNotFoundException::new);
        String VerifyCode = resetPasswordService.getData(forgotPasswordDTO.getEmail());
        if(VerifyCode != null) {
            resetPasswordService.deleteData(VerifyCode);
        }
        String code = resetPasswordService.SetResetCode(forgotPasswordDTO.getEmail());
        emailSenderService.sendRestPasswordEmail(forgotPasswordDTO.getEmail(),code);
    }

    public void VerificarCodigo(CodigoDTO codigoDTO) {
        String code = resetPasswordService.getData(codigoDTO.getEmail());
        if (Objects.equals(codigoDTO.getCodigo(), code)) {
            resetPasswordService.deleteData(codigoDTO.getEmail());
            resetPasswordService.setVerifyStatus(codigoDTO.getEmail());
        } else {
            throw new CodeNotFoundException("El codigo no es valido");
        }
    }
    public void CambiarContraseña(ResetPasswordDTO resetPasswordDTO){
        String email = resetPasswordDTO.getEmail();
        authRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        String VerifyStatus = resetPasswordService.getData(resetPasswordDTO.getEmail());
        if (!Objects.equals(resetPasswordDTO.getPassword(), resetPasswordDTO.getConfirmPassword())){
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }
        if (VerifyStatus == null ||  !VerifyStatus.equals("verified")){
            throw new IllegalArgumentException();
        }
        Credenciales credenciales = credencialesRepository.getPasswordByEmail(email);
        boolean contraseñaDescifrada = passwordEncoder.matches(resetPasswordDTO.getPassword(), credenciales.getContraseña());
        if (contraseñaDescifrada){
            throw new IllegalArgumentException("La contraseña actual es igual a la nueva");
        }
        credenciales.setContraseña(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        credencialesRepository.save(credenciales);
        resetPasswordService.deleteData(email);
    }

}
