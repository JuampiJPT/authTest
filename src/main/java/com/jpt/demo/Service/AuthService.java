package com.jpt.demo.Service;

import com.jpt.demo.Repository.UserRepository;
import com.jpt.demo.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${software.security.jwt.secret-key}")
    private String secretKey;

    @Value("${software.security.jwt.expiration-in-minutes}")
    private Long expirationInMinutes;

    /**
     * Genera un token JWT con el userId como claim.
     */
    public String generateToken(Usuario user) {
        long expirationInMillis = 1000L * 60L * expirationInMinutes;

        return Jwts.builder()
                .claim("userId", user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMillis))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }
}
