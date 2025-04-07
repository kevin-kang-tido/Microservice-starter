package com.apdbank.user.fearture.mail;

import com.apdbank.user.domain.User;
import com.apdbank.user.fearture.mail.dto.MailRequest;

public interface MailService {

    void sendMail(MailRequest mailRequest);

    void sendTokenForEmailVerification(User user, String verificationToken);

    void welcome(User user);

   void thankYouEmail(User user,String type);
}
