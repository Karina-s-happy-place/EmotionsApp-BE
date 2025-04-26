package dev.karina.emotions_app_BE.auth.service;

import dev.karina.emotions_app_BE.auth.dto.AuthResponse;
import dev.karina.emotions_app_BE.auth.dto.LoginRequest;
import dev.karina.emotions_app_BE.auth.dto.RegisterRequest;
import dev.karina.emotions_app_BE.auth.util.JwtUtil;
import dev.karina.emotions_app_BE.user.model.User;
import dev.karina.emotions_app_BE.user.model.repository.UserRepository;

import java.util.ArrayList;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest request) {
        try {
            System.out.println("Intentando autenticar al usuario: " + request.getEmail());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            System.out.println("Autenticación exitosa para el usuario: " + request.getEmail());

            UserDetails userDetails = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            System.out.println("Generando token JWT para el usuario: " + request.getEmail());

            String token = jwtUtil.generateToken(userDetails);
            return new AuthResponse(token);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Credenciales inválidas");
        } catch (Exception e) {
            System.out.println("Error durante la autenticación: " + e.getMessage());
            throw new RuntimeException("Error en la autenticación: " + e.getMessage());
        }
    }

    public AuthResponse register(RegisterRequest request) {
        // Validación de correo electrónico
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Correo electrónico ya está registrado");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        // Crear un nuevo usuario
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Codificar la contraseña

        userRepository.save(user); // Guardar el usuario en la base de datos

        // Generar el token para el usuario registrado
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>());
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token);
    }

}
