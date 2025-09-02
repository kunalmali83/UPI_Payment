package com.example.project.service;


import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;

    public OtpService(RedisTemplate<String, String> redisTemplate, JavaMailSender mailSender) {
        this.redisTemplate = redisTemplate;
        this.mailSender = mailSender;
    }

    // Generate OTP, store in Redis, send email
    public String generateOtp(String email) {
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000); // 6-digit OTP
        redisTemplate.opsForValue().set("OTP:" + email, otp, 5, TimeUnit.MINUTES); // expires in 5 mins
        sendOtpEmail(email, otp);
        return otp;
    }

    // Validate OTP
    public boolean validateOtp(String email, String otp) {
        String cachedOtp = redisTemplate.opsForValue().get("OTP:" + email);
        if (cachedOtp != null && cachedOtp.equals(otp)) {
            redisTemplate.delete("OTP:" + email); // delete after successful verification
            return true;
        }
        return false;
    }

    private void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Registration OTP");
        message.setText("Your OTP for registration is: " + otp + " (valid for 5 minutes)");
        System.out.println("📧 Sending OTP email...");
        System.out.println("To: " + toEmail);
        System.out.println("Subject: " + message.getSubject());
        System.out.println("Text: " + message.getText());

        // Actually send email
        try {
            mailSender.send(message);
            System.out.println("✅ Email send attempt successful!");
        } catch (Exception e) {
            System.err.println("❌ Failed to send OTP email: " + e.getMessage());
        }
    }
}

