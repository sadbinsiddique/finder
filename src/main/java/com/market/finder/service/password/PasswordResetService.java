package com.market.finder.service.password;

public interface PasswordResetService {
    /**
     * Initiates the password reset process by generating an OTP and sending it.
     * Throws IllegalArgumentException if input is invalid or user not found.
     */
    void initiatePasswordReset(String identifier);

    /**
     * Completes the password reset process by verifying the OTP and updating the password.
     * Throws IllegalArgumentException if input is invalid, OTP is incorrect, or user not found.
     */
    void completePasswordReset(String identifier, String otpCode, String newPassword);
}
