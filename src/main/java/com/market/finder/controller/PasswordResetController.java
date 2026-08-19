package com.market.finder.controller;

import com.market.finder.entity.User;
import com.market.finder.service.otp.OtpNotificationService;
import com.market.finder.service.otp.OtpService;
import com.market.finder.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class PasswordResetController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    private final OtpService otpService;
    private final OtpNotificationService otpNotificationService;
    private final UserService userService;

    public PasswordResetController(
            OtpService otpService,
            OtpNotificationService otpNotificationService,
            UserService userService) {
        this.otpService = otpService;
        this.otpNotificationService = otpNotificationService;
        this.userService = userService;
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(
            @RequestParam("identifier") String identifier,
            @RequestParam(value = "channel", defaultValue = "EMAIL") String channel,
            RedirectAttributes redirectAttributes) {

        logger.info("[FORGOT PASSWORD] OTP request received for identifier='{}', channel='{}'", identifier, channel);

        String trimmedIdentifier = identifier.trim();
        Optional<User> userOpt = userService.findByUsername(trimmedIdentifier);

        String otpCode = otpService.generateOtp(trimmedIdentifier);

        if ("SMS".equalsIgnoreCase(channel)) {
            otpNotificationService.sendOtpViaSms(trimmedIdentifier, otpCode);
            redirectAttributes.addFlashAttribute("infoMessage",
                    "A 6-digit verification OTP has been dispatched to your phone number.");
        } else {
            String targetEmail = userOpt.map(u -> u.getUsername() + "@university.edu").orElse(trimmedIdentifier);
            otpNotificationService.sendOtpViaSmtp(targetEmail, otpCode);
            redirectAttributes.addFlashAttribute("infoMessage",
                    "A 6-digit verification OTP has been sent to your email address via SMTP.");
        }

        redirectAttributes.addAttribute("identifier", trimmedIdentifier);
        return "redirect:/reset-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(
            @RequestParam(value = "identifier", required = false) String identifier,
            Model model) {
        model.addAttribute("identifier", identifier);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(
            @RequestParam("identifier") String identifier,
            @RequestParam("otpCode") String otpCode,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match. Please try again.");
            model.addAttribute("identifier", identifier);
            return "reset-password";
        }

        boolean isValid = otpService.validateOtp(identifier, otpCode);
        if (!isValid) {
            model.addAttribute("errorMessage", "Invalid or expired system OTP code. Please verify and try again.");
            model.addAttribute("identifier", identifier);
            return "reset-password";
        }

        try {
            userService.resetPassword(identifier, newPassword);
            otpService.clearOtp(identifier);
            redirectAttributes.addFlashAttribute("resetSuccess", true);
            return "redirect:/login?resetSuccess=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Password reset failed: " + e.getMessage());
            model.addAttribute("identifier", identifier);
            return "reset-password";
        }
    }
}
