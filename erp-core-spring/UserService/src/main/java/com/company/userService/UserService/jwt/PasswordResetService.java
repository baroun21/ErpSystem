package com.company.userService.UserService.jwt;

import com.company.erp.erp.entites.PasswordResetToken;
import com.company.erp.erp.entites.User;
import com.company.erp.erp.entites.request.ForgotPasswordRequest;
import com.company.userService.UserService.EmailService;
import com.company.userService.repository.PasswordResetTokenRepository;
import com.company.userService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public void createResetToken(ForgotPasswordRequest email) {

        System.out.println("Searching user with email: '" + email + "'");

        User user = userRepository.findByEmail(email.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete all existing tokens for this user
        List<PasswordResetToken> existingTokens = tokenRepository.findByUser(user);
        if (!existingTokens.isEmpty()) {
            tokenRepository.deleteAll(existingTokens);
        }

        String token = UUID.randomUUID().toString();
        tokenRepository.deleteByUser(user);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(resetToken);

        System.out.println("TOKEN SAVED FOR USER ID = " + user.getId());

        // Send email
        String resetLink = "http://localhost:8081/reset-password?token=" + token;
        String message = """
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
            <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;">
                <h2 style="color: #2c3e50;">Password Reset Request</h2>
                <p>Hi %s,</p>
                <p>We received a request to reset your password. Click the button below to reset it:</p>
                <p style="text-align: center;">
                    <a href="%s" 
                       style="background-color: #007bff; color: #ffffff; padding: 12px 20px; text-decoration: none; border-radius: 5px; display: inline-block;">
                        Reset Password
                    </a>
                </p>
                <p>If you didn't request a password reset, you can safely ignore this email.</p>
                <p style="font-size: 12px; color: #888;">This link will expire in 15 minutes.</p>
                <hr>
                <p style="font-size: 12px; color: #888;">&copy; 2025 ERP. All rights reserved.</p>
            </div>
        </body>
        </html>
        """.formatted(user.getUsername(), resetLink);
        System.out.println("Sending email to: " + user.getEmail());
        emailService.sendEmail(user.getEmail(), "Password Reset Request", message);
        System.out.println("Email sent with reset link: " + resetLink);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }


}