package com.market.finder.service.otp;

public interface OtpService {
    String generateOtp(String identifier);
    boolean validateOtp(String identifier, String code);
    void clearOtp(String identifier);
}
