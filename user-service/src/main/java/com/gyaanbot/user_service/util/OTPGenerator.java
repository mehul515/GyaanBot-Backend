package com.gyaanbot.user_service.util;

import java.util.Random;

public class OTPGenerator {

    public static String generateOTP() {
        int otp = 100000 + new Random().nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
}
