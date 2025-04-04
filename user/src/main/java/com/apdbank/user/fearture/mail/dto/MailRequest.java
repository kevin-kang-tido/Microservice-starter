package com.apdbank.user.fearture.mail.dto;

import lombok.Builder;

@Builder
public record MailRequest(
        String from,

        String senderName,

        String to,

        String subject,

        String text

) {
}
