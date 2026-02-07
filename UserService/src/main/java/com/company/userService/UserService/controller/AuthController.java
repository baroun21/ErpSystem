package com.company.userService.UserService.controller;


import com.company.userService.UserService.jwt.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // 1. بنجيب اليوزر بالدومين (عشان نضمن الـ Tenant الصح)
        UserPrincipal userPrincipal = (UserPrincipal) userDetailsService
                .loadUserByUsernameAndDomain(request.getUsername(), request.getCompanyDomain());

        // 2. التحقق من الباسورد يدوياً باستخدام الـ PasswordEncoder
        // بدل الـ AuthenticationManager الغبي اللي ميعرفش الدومين
        if (!passwordEncoder.matches(request.getPassword(), userPrincipal.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // 3. لو الباسورد صح، بنطلع التوكن
        String token = jwtUtil.generateToken(userPrincipal.getUsername(), userPrincipal.getCompanyId());

        return ResponseEntity.ok(new AuthResponse(token, "Bearer"));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        try {
            UserPrincipal existingUser = (UserPrincipal) userDetailsService
                    .loadUserByUsernameAndDomain(request.getUsername(), request.getCompanyDomain());

            if (existingUser != null) {
                return ResponseEntity.badRequest()
                        .body(new AuthResponse(null, "Username already exists in this company"));
            }
        } catch (UsernameNotFoundException e) {
            // لو ضرب Exception معناها المستخدم مش موجود، وده اللي إحنا عايزينه عشان نكمل تسجيل
        }

        // 2. تشفير الباسورد وحفظ المستخدم
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        userDetailsService.saveUser(request, encodedPassword);

        return ResponseEntity.ok(new AuthResponse(null, "User registered successfully"));
    }
}