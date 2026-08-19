package com.market.finder.controller;

import com.market.finder.service.password.PasswordResetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordResetController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(
            @RequestParam("identifier") String identifier,
            RedirectAttributes redirectAttributes) {

        logger.info("[FORGOT PASSWORD] OTP request received for identifier='{}'", identifier);

        if (identifier == null || identifier.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username or Email address is required.");
            return "redirect:/forgot-password";
        }

        try {
            passwordResetService.initiatePasswordReset(identifier);
            redirectAttributes.addFlashAttribute("infoMessage",
                    "A 6-digit verification OTP has been sent to your email address via SMTP.");
            redirectAttributes.addAttribute("identifier", identifier.trim());
            return "redirect:/reset-password";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/forgot-password";
        }
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

        if (identifier == null || identifier.trim().isEmpty()) {
            model.addAttribute("errorMessage", "Identifier is required.");
            return "reset-password";
        }
        if (otpCode == null || otpCode.trim().isEmpty()) {
            model.addAttribute("errorMessage", "OTP Code is required.");
            model.addAttribute("identifier", identifier);
            return "reset-password";
        }
        if (newPassword == null || newPassword.isEmpty()) {
            model.addAttribute("errorMessage", "New Password is required.");
            model.addAttribute("identifier", identifier);
            return "reset-password";
        }
        if (newPassword.length() < 4 || newPassword.length() > 68) {
            model.addAttribute("errorMessage", "Password must be between 68 characters.");
            model.addAttribute("identifier", identifier);
            return "reset-password";
        }
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match.");
            model.addAttribute("identifier", identifier);
            return "reset-password";
        }

        try {
            passwordResetService.completePasswordReset(identifier, otpCode, newPassword);
            redirectAttributes.addFlashAttribute("resetSuccess", true);
            return "redirect:/login?resetSuccess=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("identifier", identifier);
            return "reset-password";
        }
    }
}
