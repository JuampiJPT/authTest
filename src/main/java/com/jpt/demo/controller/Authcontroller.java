package com.jpt.demo.controller;

import com.jpt.demo.Service.UserService;
import com.jpt.demo.model.AuthRequest;
import com.jpt.demo.model.CreateUserResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/public/user")
public class Authcontroller {

    private final UserService userService;

    public Authcontroller(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para login: POST /public/user/login
     */
    @PostMapping("/login")
    public String createSession(
            @Valid @RequestBody AuthRequest authRequest,
            HttpSession session
    ) {
        // Guarda info en sesión (opcional)
        session.setAttribute("username", authRequest.getUsername());
        session.setAttribute("ultimaConexion", new Date());

        return userService.createSession(authRequest);
    }

    /**
     * Endpoint para registro: POST /public/user/register
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody AuthRequest request
    ) {
        // Simplemente delegamos en el UserService:
        System.out.println("request: " + request);
        return userService.registerUser(request);
    }
}
