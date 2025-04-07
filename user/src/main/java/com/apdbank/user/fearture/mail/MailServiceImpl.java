package com.apdbank.user.fearture.mail;


import com.apdbank.user.domain.User;
import com.apdbank.user.fearture.mail.VerificationToken.VerificationTokenRepository;
import com.apdbank.user.fearture.mail.dto.MailRequest;
import com.apdbank.user.fearture.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom  ;

    @Value("${spring.mail.senderName}")
    private String senderName;

    @Value("${spring.client-path}")
    private String clientUrl;



    @Override
    public void sendMail(MailRequest mailRequest) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true); // true enables multipart (HTML support)
            mimeMessageHelper.setFrom(mailFrom, senderName);
            mimeMessageHelper.setTo(mailRequest.to());
            mimeMessageHelper.setSubject(mailRequest.subject());
            mimeMessageHelper.setText(mailRequest.text(), true);

            mailSender.send(mimeMessage);

            log.info("Email sent successfully to {}", mailRequest.to());
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error sending email to {}: {}", mailRequest.to(), e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendTokenForEmailVerification(User user, String verificationToken) {
        String subject = "Email Verification";
        String mailContent = "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333;\">" +
                "<p style=\"font-size: 16px;\">Hi <strong>" + user.getUsername() + "</strong>,</p>"  +
                "<p style=\"font-size: 14px; color: #555;\">Here is your verification token:</p>" +
                "<p style=\"font-size: 18px; color: #000; font-weight: bold;\">" + verificationToken + "</p>" +
                "<p style=\"text-align: center; margin: 20px 0;\">" +
                "<a href=\"" + clientUrl + "\" style=\"display: inline-block; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007BFF; text-decoration: none; border-radius: 5px;\">Verify Your Email</a>" +
                "</p>" +
                "<p style=\"font-size: 14px;\">If you have any questions, feel free to reach out to us.</p>" +
                "<p style=\"font-size: 14px;\">Thank you,<br><strong>Team</strong></p>" +
                "</div>";

        MailRequest mailRequest = MailRequest.builder()
                .from(mailFrom)
                .senderName(senderName)
                .to(user.getEmail())
                .subject(subject)
                .text(mailContent)
                .build();

        // TODO: all if it
        sendMail(mailRequest);
    }

    @Override
    public void welcome(User user) {
        String subject = "Welcome to the MicroService Plaltform";
        String mailContent = "<p>Hi " + user.getUsername() + ",</p>" +
                "<p>Thank you for joining our platform. We’re excited to have you on board.</p>" +
                "<p>Explore our features and let us know if you have any questions.</p>" +
                "<p>Best regards,<br>Team</p>";

        MailRequest mailRequest = MailRequest.builder()
                .from(mailFrom)
                .senderName(senderName)
                .to(user.getEmail())
                .subject(subject)
                .text(mailContent)
                .build();
        sendMail(mailRequest);
    }

    @Override
    public void thankYouEmail(User user, String type) {
        String subject = "Thank you  Register you Email";
        String anyQ =
                "<p> Let Me know if you have any question.</p>" +
                "<p>Best regards,<br>Team</p>";

        MailRequest mailRequest = MailRequest.builder()
                .from(mailFrom)
                .senderName(senderName)
                .to(user.getEmail())
                .subject(subject)
                .text(anyQ)
                .build();

        sendMail(mailRequest);
    }

}
