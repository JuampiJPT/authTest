package com.jpt.demo.Service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpt.demo.Repository.UserRepository;
import com.jpt.demo.model.AuthRequest;
import com.jpt.demo.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String secretKey;
    private final Long expiration;
    private final ObjectMapper objectMapper;
    private final String saltKey;

    public AuthService(UserRepository userRepository,
                       @Value("${software.security.jwt.secret-key}") String secretKey,
                       @Value("${software.security.salt.key}") String saltKey,
                       @Value("${software.security.jwt.expiration-in-minutes}") Long expirationInMinutes) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.secretKey = secretKey;
        this.expiration = 1000L * 60L * expirationInMinutes;
        this.objectMapper = new ObjectMapper();
        this.saltKey = saltKey;
    }

    public String login(AuthRequest authRequest) {
        Optional<Usuario> opUser = userRepository.findByLogin(authRequest.getUsername());

        if (opUser.isEmpty() || !passwordEncoder.matches(authRequest.getPassword(), opUser.get().getClave())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        Usuario userEntity = opUser.get();
        return generateToken(userEntity);
    }

    public Usuario register(Usuario user) {
        user.setClave(passwordEncoder.encode(user.getClave()));
        return userRepository.save(user);
    }

    private String generateToken(Usuario user) {
        return Jwts.builder()
                .claim("userId", user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }

    public String createRefreshToken(Usuario user) {
        return DigestUtils.sha256Hex(user.getId() + user.getLogin() + user.getClave() + saltKey);
    }
}

