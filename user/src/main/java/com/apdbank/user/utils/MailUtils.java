package com.apdbank.user.utils;

import java.security.SecureRandom;

public class MailUtils {
    // Method to generate a numeric token
    public static String generateDigitsToken() {
        SecureRandom random = new SecureRandom();
        int tokenLength = 6;
        StringBuilder token = new StringBuilder(tokenLength);

        for (int i = 0; i < tokenLength; i++) {
            int digit = random.nextInt(10);
            token.append(digit);
        }
        return token.toString();
    }
    public enum AccountUpdateType {
        PROFILE_IMAGE,
        CHANGE_PASSWORD
    }
}
