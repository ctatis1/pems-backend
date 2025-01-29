package com.ems.backend.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ems.backend.entities.AuthUserDto;
import com.ems.backend.entities.Usuario;
import com.ems.backend.repositories.UsuarioRepository;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    public AuthService(
        UsuarioRepository usuarioRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario signup(AuthUserDto input) {
        Usuario user = new Usuario();
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole("EXTERNAL");

        return usuarioRepository.save(user);
    }

    public Usuario authenticate(AuthUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return usuarioRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }
}
