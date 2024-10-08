package com.api.reservavuelos.Security;

import com.api.reservavuelos.Exceptions.UserNotFoundException;
import com.api.reservavuelos.Models.Roles;
import com.api.reservavuelos.Models.Usuarios;
import com.api.reservavuelos.Repositories.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService  implements UserDetailsService {
    private final AuthRepository authRepository;
    @Autowired
    public CustomUserDetailsService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Collection<GrantedAuthority> mapToAuthorities(List<Roles> roles){
    return roles.stream().map(role ->new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
    }
    @Override
    public UserDetails loadUserByUsername(String Email) throws UserNotFoundException {
        Usuarios usuarios = authRepository.findByEmail(Email).orElseThrow(UserNotFoundException::new);
        String password = authRepository.findPasswordByEmail(Email).orElseThrow(UserNotFoundException::new);
        return new User(usuarios.getEmail(), password, mapToAuthorities(usuarios.getRoles()));
    }
}
