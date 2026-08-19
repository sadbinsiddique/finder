package com.market.finder.service.otp;

public interface OtpNotificationService {
    void sendOtpViaSmtp(String toEmail, String otpCode);
    void sendOtpViaSms(String phoneNumber, String otpCode);
}
