package com.apdbank.user.fearture.mail.VerificationToken;

import com.apdbank.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity

@Table(name = "email_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    private String verificationToken;

    private String token;

   private Boolean isUsed;

   private Date expirationTime;

   private Boolean isExpired;

   private static final  int EXPIRATION_TIME = 5;

   private LocalTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType type;

    public enum TokenType {
        EMAIL_VERIFICATION,
        FORGET_PASSWORD
    }

    public VerificationToken(String token, TokenType type) {
        super();
        this.createdAt = LocalTime.now();
        this.token = token;
        this.type = type;
        this.isUsed = false;
        this.expirationTime = this.getExpirationTime();
    }


    public Date getExpirationTime() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());

    }

    public boolean isExpired() {
        return new Date().after(this.expirationTime);
    }
}