package com.apdbank.user.fearture.mail;


import com.apdbank.user.domain.User;
import com.apdbank.user.fearture.mail.dto.MailRequest;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    @Override
    public void sendMail(MailRequest mailRequest) {

    }

    @Override
    public void sendTokenForEmailVerification(User user, String verificationToken) {

    }

    @Override
    public void welcome(User user) {

    }
}
