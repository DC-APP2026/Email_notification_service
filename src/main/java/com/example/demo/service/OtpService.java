package com.example.demo.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final SecureRandom random = new SecureRandom();

    public String generateOtp() {

        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
}