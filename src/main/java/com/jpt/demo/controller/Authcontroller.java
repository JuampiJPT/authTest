package com.jpt.demo.controller;

import com.jpt.demo.Service.AuthService;
import com.jpt.demo.model.AuthRequest;
import com.jpt.demo.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class Authcontroller {

    private final AuthService authService;

    public Authcontroller(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario user) {
        return ResponseEntity.ok(authService.register(user));
    }
}