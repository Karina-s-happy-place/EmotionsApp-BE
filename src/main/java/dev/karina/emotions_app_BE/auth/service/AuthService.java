package dev.karina.emotions_app_BE.auth.service;

import dev.karina.emotions_app_BE.auth.dto.AuthResponse;
import dev.karina.emotions_app_BE.auth.dto.LoginRequest;
import dev.karina.emotions_app_BE.auth.dto.RegisterRequest;
import dev.karina.emotions_app_BE.auth.util.JwtUtil;
import dev.karina.emotions_app_BE.user.model.User;
import dev.karina.emotions_app_BE.user.model.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public AuthService(AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse login(LoginRequest request) {
        try {
            System.out.println("Intentando autenticar al usuario: " + request.getEmail());
            System.out.println("Contraseña ingresada: " + request.getPassword());
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            User user = (User) userDetails;
            System.out.println("Contraseña almacenada: " + user.getPassword());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(), request.getPassword()));
            String token = jwtUtil.generateToken(userDetails);
            System.out.println("Token generado: " + token);
            return new AuthResponse(token);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Credenciales inválidas");
        } catch (Exception e) {
            throw new RuntimeException("Error en la autenticación: " + e.getMessage());
        }
    }

    public AuthResponse register(RegisterRequest request) {
        try {
            if (userRepository.existsByEmail(request.getEmail()))
                throw new RuntimeException("Correo electrónico ya está registrado");
            if (!request.getPassword().equals(request.getConfirmPassword()))
                throw new RuntimeException("Las contraseñas no coinciden");
            User user = new User();
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getEmail(), user.getPassword(), Collections.emptyList());
            String token = jwtUtil.generateToken(userDetails);
            return new AuthResponse(token);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario. Verifica los datos.");
        }
    }
}
