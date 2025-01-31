package com.jpt.demo.Service;

import com.jpt.demo.Repository.UserRepository;
import com.jpt.demo.model.AuthRequest;
import com.jpt.demo.model.CreateUserResponse;
import com.jpt.demo.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // BCryptPasswordEncoder o lo que uses
    private final AuthService authService;         // Para generar el token

    /**
     * Inicia sesión: busca el usuario por username, valida la contraseña
     * y retorna un CreateUserResponse con el token.
     */
    public String createSession(AuthRequest authRequest) {
        Optional<Usuario> opUser = userRepository.findByLogin(authRequest.getUsername());
        if (opUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenciales inválidas");
        }

        Usuario userEntity = opUser.get();
        System.out.println("User: " + userEntity.getLogin());

        if (!passwordEncoder.matches(authRequest.getPassword(), userEntity.getClave())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenciales inválidas");
        }

        return authService.generateToken(userEntity);
    }

    /**
     * Registra un nuevo usuario en la BD, y opcionalmente retorna el token.
     */
    public ResponseEntity<String> registerUser(AuthRequest request) {
        // Verificar si el username ya existe
        if (userRepository.findByLogin(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario ya existe");
        }

        // Crear la entidad Usuario y setear login y clave
        Usuario newUser = new Usuario();
        newUser.setLogin(request.getUsername());
        System.out.println("request.setLogin: " + newUser.getLogin());
        newUser.setClave(passwordEncoder.encode(request.getPassword()));
        System.out.println("request.setLogin: " + newUser.getClave());


        // Guardar en BD
        System.out.println("newUser: " + newUser);
        userRepository.save(newUser);

        // Retornar CreateUserResponse con token (opcional)
        if (newUser.getClave() != null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}