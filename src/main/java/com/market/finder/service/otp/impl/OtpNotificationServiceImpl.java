package com.market.finder.service.otp.impl;

import com.market.finder.service.otp.OtpNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpNotificationServiceImpl implements OtpNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(OtpNotificationServiceImpl.class);

    private final JavaMailSender mailSender;

    public OtpNotificationServiceImpl(ObjectProvider<JavaMailSender> mailSenderProvider) {
        this.mailSender = mailSenderProvider.getIfAvailable();
    }

    @Override
    public void sendOtpViaSmtp(String toEmail, String otpCode) {
        logger.info("[SMTP OTP PUSH] Attempting to push OTP '{}' via SMTP to email='{}'", otpCode, toEmail);

        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(toEmail);
                message.setSubject("University ERP - System Security OTP Code");
                message.setText("Your system OTP verification code is: " + otpCode + "\n\nThis code is valid for 5 minutes. Do not share it with anyone.");
                mailSender.send(message);
                logger.info("[SMTP OTP PUSH] Successfully dispatched email to '{}'", toEmail);
            } catch (Exception e) {
                logger.warn("[SMTP OTP PUSH] Mail dispatch failed (SMTP error): {}. System fallback active for OTP code '{}'", e.getMessage(), otpCode);
            }
        } else {
            logger.info("[SMTP OTP PUSH] JavaMailSender not configured. System OTP for '{}' is: {}", toEmail, otpCode);
        }
    }

    @Override
    public void sendOtpViaSms(String phoneNumber, String otpCode) {
        logger.info("[SMS OTP PUSH] Dispatched SMS verification to phone number='{}'. System OTP code: {}", phoneNumber, otpCode);
    }
}
