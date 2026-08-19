package com.market.finder.service.password.impl;

import com.market.finder.entity.User;
import com.market.finder.service.otp.OtpNotificationService;
import com.market.finder.service.otp.OtpService;
import com.market.finder.service.password.PasswordResetService;
import com.market.finder.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserService userService;
    private final OtpService otpService;
    private final OtpNotificationService otpNotificationService;

    public PasswordResetServiceImpl(
            UserService userService,
            OtpService otpService,
            OtpNotificationService otpNotificationService) {
        this.userService = userService;
        this.otpService = otpService;
        this.otpNotificationService = otpNotificationService;
    }

    @Override
    @Transactional
    public void initiatePasswordReset(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("Identifier is required.");
        }
        String trimmedIdentifier = identifier.trim();
        Optional<User> userOpt = userService.findUserByIdentifier(trimmedIdentifier);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User account not found. Please verify your details.");
        }

        String username = userOpt.get().getUsername();
        String otpCode = otpService.generateOtp(username);

        String targetEmail = trimmedIdentifier.contains("@") ? trimmedIdentifier : userService.getEmailByUsername(username);
        otpNotificationService.sendOtpViaSmtp(targetEmail, otpCode);
    }

    @Override
    @Transactional
    public void completePasswordReset(String identifier, String otpCode, String newPassword) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("Identifier is required.");
        }
        if (otpCode == null || otpCode.isBlank()) {
            throw new IllegalArgumentException("OTP Code is required.");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New Password is required.");
        }

        String trimmedIdentifier = identifier.trim();
        Optional<User> userOpt = userService.findUserByIdentifier(trimmedIdentifier);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User account not found.");
        }

        String username = userOpt.get().getUsername();
        boolean isValid = otpService.validateOtp(username, otpCode);
        if (!isValid) {
            throw new IllegalArgumentException("Invalid OTP code.");
        }

        userService.resetPassword(username, newPassword);
        otpService.clearOtp(username);
    }
}
