package com.psb.psb.config;

import com.psb.psb.entities.Role;
import com.psb.psb.entities.User;
import com.psb.psb.repositories.RoleRepository;
import com.psb.psb.repositories.UserRepository;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                    jwt.decoder(jwtDecoder());
                }))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner init(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder) {
        return args -> {
            // Créer rôle SUPERADMIN et ROLE_USER s’ils n’existent pas
            Role superAdminRole = roleRepo.findByName("ROLE_SUPERADMIN").orElseGet(() -> {
                Role r = new Role();
                r.setName("ROLE_SUPERADMIN");
                return roleRepo.save(r);
            });
            Role userRole = roleRepo.findByName("ROLE_USER").orElseGet(() -> {
                Role r = new Role();
                r.setName("ROLE_USER");
                return roleRepo.save(r);
            });

            // Créer le superAdmin s’il n’existe pas
            if (userRepo.findByUsername("superadmin").isEmpty()) {
                System.out.println("Creating superadmin user..... <3");
                User admin = new User();
                admin.setUsername("superadmin");
                admin.setPassword(encoder.encode("pass"));
                admin.roles().add(superAdminRole);
                userRepo.save(admin);
            }
        };
    }
}

