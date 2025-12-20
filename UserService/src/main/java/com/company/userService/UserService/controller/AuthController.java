package com.company.userService.UserService.controller;


import com.company.userService.UserService.jwt.AuthRequest;
import com.company.userService.UserService.jwt.AuthResponse;
import com.company.userService.UserService.jwt.CustomUserDetailsService;
import com.company.userService.UserService.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for login and registration")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Login with username and password", description = "Returns a JWT token if authentication is successful")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtUtil.generateToken(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token, "Bearer"));
    }

    @Operation(summary = "Register a new user", description = "Registers a new user and returns a success message or error if username exists")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {

        // Check if user already exists
        if (userDetailsService.userExists(request.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(null, "Username already exists"));
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Save user
        userDetailsService.saveUser(request, encodedPassword);

//        // Generate JWT token after registration (optional)
//        String token = jwtUtil.generateToken(request.getUsername());

        // Return clean JSON
        return ResponseEntity.ok(new AuthResponse(null, "User registered successfully"));
    }

}
