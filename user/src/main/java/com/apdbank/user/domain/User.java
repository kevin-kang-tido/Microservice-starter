package com.apdbank.user.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(unique = true,nullable = false)
    private String uuid;

   @Column(unique = true,nullable = false,length = 120)
    private String username;

   @Column(name = "images")
   private String avatar;

    @Column(length = 120)
    private String firstName;

    @Column(length = 120)
    private String lastName;

    @Column(unique = true,nullable = false,length =30)
    private String phoneNumber;

    @Column(unique = true,nullable = false,length = 200)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String comfirmPassword;

    @Column(length = 30)
    private String gender;

    private boolean isEmailVerified;

    private boolean isDeleted; // TODO: delete form the database

    @Column(nullable = false)
    private boolean isBlocked; // TODO: block can't use account for a prious time

    private boolean isAccountNonExpired;  // TODO: user acc is not Expired

    private boolean isAccountNonLocked;   // TODO: user acc is not locked

    /**
     * Indicates if the user's credentials are not expired.
     */
    private boolean isCredentialsNonExpired;

    @Column(updatable = false,nullable = false)
    private LocalTime createAt;  // TODO: current time createAt acc

    private LocalTime updateAt;

    private LocalTime lastLoginAt;

    @ManyToMany(fetch = FetchType.EAGER) // ✅ Load roles immediately
    private List<Role> roles;




}
