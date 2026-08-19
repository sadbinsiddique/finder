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
}
