package com.api.reservavuelos.Services;
import com.api.reservavuelos.DTO.Cache.AuthenticationCacheDTO;
import com.api.reservavuelos.DTO.Cache.ProfileCacheDTO;
import com.api.reservavuelos.DTO.Request.*;
import com.api.reservavuelos.DTO.Response.AuthResponseDTO;
import com.api.reservavuelos.DTO.Response.ResponseDTO;
import com.api.reservavuelos.Exceptions.*;
import com.api.reservavuelos.Models.*;
import com.api.reservavuelos.Repositories.*;
import com.api.reservavuelos.Security.JwtTokenProvider;
import com.api.reservavuelos.Utils.DateFormatter;
import com.api.reservavuelos.Utils.QRCodeGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.hc.client5.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CredencialesRepository credencialesRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final ResetPasswordService resetPasswordService;
    private final ProfileImageRepository profileImageRepository;
    private final DateFormatter dateFormatter;
    private final GoogleAuthenticatorService googleAuthenticatorService;
    private final TwoFactorAuthRepository twoFactorAuthRepository;
    private final QRCodeGenerator qrCodeGenerator;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository,
                       RolRepository rolRepository,
                       CredencialesRepository credencialesRepository,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       EmailSenderService emailSenderService,
                       ResetPasswordService resetPasswordService,
                       ProfileImageRepository profileImageRepository,
                       DateFormatter dateFormatter,
                       GoogleAuthenticatorService googleAuthenticatorService,
                       TwoFactorAuthRepository twoFactorAuthRepository,
                       QRCodeGenerator qrCodeGenerator,
                       RedisTemplate<String, Object> redisTemplate,
                       ObjectMapper objectMapper) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.credencialesRepository = credencialesRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.emailSenderService = emailSenderService;
        this.resetPasswordService = resetPasswordService;
        this.profileImageRepository = profileImageRepository;
        this.dateFormatter = dateFormatter;
        this.googleAuthenticatorService = googleAuthenticatorService;
        this.twoFactorAuthRepository = twoFactorAuthRepository;
        this.qrCodeGenerator = qrCodeGenerator;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }


    public AuthResponseDTO registrarUsuario(RegisterRequestDTO registerRequestDTO, HttpServletRequest request){
       if (usuarioRepository.existsByEmail(registerRequestDTO.getEmail())){
           throw new UserAlreadyRegisterException();
       }
        Roles roles = rolRepository.findByNombre("usuario").orElseThrow(() -> new NoSuchElementException("No se encontro el rol usuario"));
       Usuarios usuario = new Usuarios();
       usuario.setPrimer_nombre(registerRequestDTO.getPrimer_nombre());
       usuario.setSegundo_nombre(registerRequestDTO.getSegundo_nombre());
       usuario.setPrimer_apellido(registerRequestDTO.getPrimer_apellido());
       usuario.setSegundo_apellido(registerRequestDTO.getSegundo_apellido());
       usuario.setEmail(registerRequestDTO.getEmail());
       usuario.setTelefono(registerRequestDTO.getTelefono());
       usuario.setFecha_nacimiento(registerRequestDTO.getFecha_nacimiento());
       usuario.setGenero(registerRequestDTO.getGenero());
        Profile_image profileImage = new Profile_image();
        profileImage.setImage_url("https://t4.ftcdn.net/jpg/00/64/67/63/360_F_64676383_LdbmhiNM6Ypzb3FM4PPuFP9rHe7ri8Ju.jpg");
        profileImageRepository.save(profileImage);
        usuario.setProfile_image(profileImage);
       Credenciales credencial = new Credenciales();
       credencial.setContraseña(passwordEncoder.encode(registerRequestDTO.getContraseña()));
       credencialesRepository.save(credencial);
       usuario.setCredenciales(credencial);
       usuario.setRoles(Collections.singletonList(roles));
       usuarioRepository.save(usuario);
       return new AuthResponseDTO(dateFormatter.formatearFecha(), "P-201", usuario.getId(), "Usuario creado correctamente", request.getRequestURI());
    }

    public AuthResponseDTO login(LoginRequestDTO dtoLogin, HttpServletRequest request) throws InvalidCredentialsException {
                Optional<Usuarios> usuarioOptional = usuarioRepository.findByEmail(dtoLogin.getEmail());
                if (usuarioOptional.isEmpty()) {
                    throw new UserNotFoundException();
                }

                Usuarios usuario = usuarioOptional.get();
                Credenciales credenciales = credencialesRepository.getPasswordByEmail(usuario.getEmail());
                Optional<TwoFactorAuth> TwoFactorAuth = twoFactorAuthRepository.findByid_usuario(usuario.getId());
                 if(TwoFactorAuth.isEmpty() && usuario.getRoles().stream().anyMatch(rol -> rol.getNombre().equals("administrador"))){
                    throw new IllegalStateException("Los administradores deben tener el 2FA Activado para poder iniciar sesion");
        }
                boolean PasswordMatch = passwordEncoder.matches(dtoLogin.getContraseña(), credenciales.getContraseña());
                if (!PasswordMatch) {
                    throw new InvalidCredentialsException("Contraseña incorrecta");
                }
                if(TwoFactorAuth.isPresent()){
                    AuthenticationCacheDTO authDTO = new AuthenticationCacheDTO();
                    authDTO.setEmail(dtoLogin.getEmail());
                    authDTO.setContraseña(dtoLogin.getContraseña());
                    redisTemplate.opsForValue().set(dtoLogin.getEmail() + "2FA", authDTO, 5, TimeUnit.MINUTES );
                    return new AuthResponseDTO(dateFormatter.formatearFecha(), "P-200", usuario.getId(), "El usuario se ha logeado pero, tiene activado el 2FA, debe introducir el codigo", request.getRequestURI());
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dtoLogin.getEmail(), dtoLogin.getContraseña()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return new AuthResponseDTO(dateFormatter.formatearFecha(), "200", usuario.getId(), token, request.getRequestURI());
    }

    public ResponseDTO OlvidarContraseña(ForgotPasswordRequestDTO forgotPasswordRequestDTO, HttpServletRequest request){
        usuarioRepository.findByEmail(forgotPasswordRequestDTO.getEmail()).orElseThrow(UserNotFoundException::new);
        String VerifyCode = resetPasswordService.getData(forgotPasswordRequestDTO.getEmail());
        if(VerifyCode != null) {
            resetPasswordService.deleteData(VerifyCode);
        }
        String code = resetPasswordService.SetResetCode(forgotPasswordRequestDTO.getEmail());
        emailSenderService.sendRestPasswordEmail(forgotPasswordRequestDTO.getEmail(),code);
        return setResponseDTO("P-200", "Se ha enviado un codigo de verificacion al correo electronico registrado", request);
    }

    public ResponseDTO VerificarCodigo(CodigoRequestDTO codigoRequestDTO, HttpServletRequest request) {
        String code = resetPasswordService.getData(codigoRequestDTO.getEmail());
        if (!Objects.equals(codigoRequestDTO.getCodigo(), code)) {
            throw new CodeNotFoundException("El codigo no es valido");
        }
        resetPasswordService.deleteData(codigoRequestDTO.getEmail());
        resetPasswordService.setVerifyStatus(codigoRequestDTO.getEmail());
        return setResponseDTO("P-200", "El codigo de verificacion fue validado correctamente", request);
    }
    public ResponseDTO CambiarContraseña(ResetPasswordRequestDTO resetPasswordRequestDTO, HttpServletRequest request){
        String email = resetPasswordRequestDTO.getEmail();
        usuarioRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        String VerifyStatus = resetPasswordService.getData(resetPasswordRequestDTO.getEmail());
        if (!Objects.equals(resetPasswordRequestDTO.getPassword(), resetPasswordRequestDTO.getConfirmPassword())){
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }
        if (VerifyStatus == null ||  !VerifyStatus.equals("verified")){
            throw new IllegalArgumentException("No tienes permiso para realizar esta accion");
        }
        Credenciales credenciales = credencialesRepository.getPasswordByEmail(email);
        boolean contraseñaDescifrada = passwordEncoder.matches(resetPasswordRequestDTO.getPassword(), credenciales.getContraseña());
        if (contraseñaDescifrada){
            throw new IllegalArgumentException("La contraseña actual es igual a la nueva");
        }
        credenciales.setContraseña(passwordEncoder.encode(resetPasswordRequestDTO.getPassword()));
        credencialesRepository.save(credenciales);
        resetPasswordService.deleteData(email);
        return setResponseDTO("P-200", "Contraseña cambiada correctamente", request);
    }
    public ResponseDTO TotpSetup(Long id_usuario, HttpServletRequest request) throws Exception {
        Optional<TwoFactorAuth> twoFactorAuthOptional = twoFactorAuthRepository.findByid_usuario(id_usuario);
        if (twoFactorAuthOptional.isPresent()){
            throw new Exception("Ya tienes seteado el 2FA");
        }
        Usuarios usuario = usuarioRepository.getById(id_usuario);
        TwoFactorAuth twoFactor = new TwoFactorAuth();
        twoFactor.setSecretKey(googleAuthenticatorService.generateSecretKey());
        twoFactor.setUsuarios(usuario);
        twoFactorAuthRepository.save(twoFactor);
        String qrCodeurl = qrCodeGenerator.getQRCodeURL(id_usuario,twoFactor.getSecretKey());
        byte[] QRcode = qrCodeGenerator.generateQRCodeImage(qrCodeurl);
        System.out.println(usuario.getEmail());
        emailSenderService.sendEmailWithQRCode(usuario.getEmail(), QRcode);
      return setResponseDTO("200", "Se envio el qr  para activar el 2AF al email registrado", request);
    }

    public AuthResponseDTO TotpVerification (Long id_usuario, Codigo2FARequestDTO codigo2FARequestDTO, HttpServletRequest request) throws JsonProcessingException {
        Optional<TwoFactorAuth> twoFactorAuthOptional = twoFactorAuthRepository.findByid_usuario(id_usuario);
        Optional<Usuarios> usuarioOptional = usuarioRepository.findById(id_usuario);
        Usuarios usuario = usuarioOptional.get();
        AuthenticationCacheDTO userCache = (AuthenticationCacheDTO) redisTemplate.opsForValue().get(usuario.getEmail() + "2FA");
        if (userCache == null){
            throw new UnauthorizedException("No estas autorizado para usar esto");
        }
        if (twoFactorAuthOptional.isEmpty()){
          throw new IllegalArgumentException("No tienes activado el A2F");
        }
        TwoFactorAuth twoFactorAuth = twoFactorAuthOptional.get();
        boolean CodeValidate = googleAuthenticatorService.validateCode(twoFactorAuth.getSecretKey(), codigo2FARequestDTO.getCodigo());
        if (CodeValidate){
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userCache.getEmail(), userCache.getContraseña()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
           redisTemplate.delete("2FA"+ usuario.getEmail());
            return new AuthResponseDTO(dateFormatter.formatearFecha(), "200", id_usuario, token, request.getRequestURI());
        } else {
            throw new Code2FAException("El codigo de verificacion no es valido");
        }
    }

    private ResponseDTO setResponseDTO(String code, String message, HttpServletRequest request){
        return new ResponseDTO(dateFormatter.formatearFecha(), code, message, request.getRequestURI());
    }


}
