package com.ems.backend.controllers;
 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.backend.entities.AuthUserDto;
import com.ems.backend.entities.LoginResp;
import com.ems.backend.entities.Usuario;
import com.ems.backend.services.AuthService;
import com.ems.backend.services.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;
    
    private final AuthService authService;

    public AuthController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody AuthUserDto authUserDto) {
        authService.signup(authUserDto);
        return ResponseEntity.ok("Usuario creado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResp> authenticate(@RequestBody AuthUserDto authUserDto) {
        Usuario authenticatedUser = authService.authenticate(authUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResp loginResponse = new LoginResp();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
