package com.company.userService.UserService.controller;

import com.company.erp.erp.entites.request.ForgotPasswordRequest;
import com.company.erp.erp.entites.request.ResetPasswordRequest;
import com.company.userService.UserService.jwt.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Password Reset", description = "Endpoints for password reset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @Operation(summary = "Request password reset link")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest email) {
        System.out.println("Email received: " + email.getEmail());
        passwordResetService.createResetToken(email);
        return ResponseEntity.ok("If the email exists, a reset link has been sent");
    }

    @Operation(summary = "Reset password using token")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        passwordResetService.resetPassword(
                request.getToken(),
                request.getNewPassword()
        );

        return ResponseEntity.ok("Password updated successfully");
    }

}