package com.psb.psb.services;


import com.psb.psb.entities.Role;
import com.psb.psb.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    // Durée de validité du token en millisecondes (ici 24h)
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000L;

    public String login(String username, String rawPassword) {
        // Récupérer l'utilisateur
        User user = userService.findByUsername(username);

        // Vérifier le mot de passe
        if (!passwordEncoder.matches(rawPassword, user.password())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Extraire les rôles de l'utilisateur
        var roles = user.roles().stream()
                .map(Role::name)
                .collect(Collectors.toList());

        // Générer le token JWT
        var now = new Date();
        var expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        String token = Jwts.builder()
                .setSubject(user.username()) // le sujet est généralement le username
                .claim("roles", roles) // ajout d'une claim roles
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }
}
