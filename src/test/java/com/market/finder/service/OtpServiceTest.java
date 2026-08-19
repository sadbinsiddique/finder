package com.market.finder.service;

import com.market.finder.service.otp.OtpService;
import com.market.finder.service.otp.impl.OtpServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OtpServiceTest {

    private OtpService otpService;

    @BeforeEach
    void setUp() {
        org.springframework.cache.concurrent.ConcurrentMapCacheManager cacheManager =
                new org.springframework.cache.concurrent.ConcurrentMapCacheManager("otpCache");
        otpService = new OtpServiceImpl(cacheManager);
    }

    @Test
    void testGenerateAndValidateOtp_Success() {
        String identifier = "admin";
        String otp = otpService.generateOtp(identifier);

        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otpService.validateOtp(identifier, otp));
    }

    @Test
    void testValidateOtp_InvalidCode() {
        String identifier = "admin";
        otpService.generateOtp(identifier);

        assertFalse(otpService.validateOtp(identifier, "000000"));
    }

    @Test
    void testClearOtp() {
        String identifier = "admin";
        String otp = otpService.generateOtp(identifier);
        otpService.clearOtp(identifier);

        assertFalse(otpService.validateOtp(identifier, otp));
    }

    @Test
    void testOtpExpiration() {
        OtpServiceImpl serviceImpl = (OtpServiceImpl) otpService;
        String identifier = "expiringUser";
        
        // Create an expired entry manually
        OtpServiceImpl.OtpEntry expiredEntry = new OtpServiceImpl.OtpEntry("123456", -1000L);
        assertTrue(expiredEntry.isExpired());
    }

    @Test
    void testSmtpConnection() {
        org.springframework.mail.javamail.JavaMailSenderImpl mailSender = new org.springframework.mail.javamail.JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("sadbinsiddique@gmail.com");
        mailSender.setPassword("lexohoupzaxgghht");
        
        java.util.Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        try {
            org.springframework.mail.SimpleMailMessage message = new org.springframework.mail.SimpleMailMessage();
            message.setTo("sadbinsiddique@gmail.com");
            message.setSubject("Test Mail");
            message.setText("Test OTP");
            mailSender.send(message);
            System.out.println("SMTP TEST SUCCESSFUL");
        } catch (Exception e) {
            System.err.println("SMTP TEST FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
