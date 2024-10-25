package com.api.reservavuelos.Security;

import com.api.reservavuelos.Exceptions.UserNotFoundException;
import com.api.reservavuelos.Models.Roles;
import com.api.reservavuelos.Models.Usuarios;
import com.api.reservavuelos.Repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService  implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final HandlerExceptionResolver resolver;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    @Autowired
    public CustomUserDetailsService(UsuarioRepository usuarioRepository,
                                    @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        this.usuarioRepository = usuarioRepository;
        this.resolver = resolver;
        this.request = request;
        this.response = response;
    }

    public Collection<GrantedAuthority> mapToAuthorities(List<Roles> roles){
    return roles.stream().map(role ->new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
    }
    @Override
    public UserDetails loadUserByUsername(String Email) throws UserNotFoundException {
        try {
            Usuarios usuarios = usuarioRepository.findByEmail(Email).orElseThrow(UserNotFoundException::new);
            String password = usuarioRepository.findPasswordByEmail(Email).orElseThrow(UserNotFoundException::new);
            return new User(usuarios.getEmail(), password, mapToAuthorities(usuarios.getRoles()));
        } catch (UserNotFoundException e){
            resolver.resolveException(request, response, null, e);
            throw e;
        }
    }
}
