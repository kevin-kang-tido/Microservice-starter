package com.apdbank.user.fearture.user;

import com.apdbank.user.base.BasedMessage;
import com.apdbank.user.domain.Role;
import com.apdbank.user.domain.User;
import com.apdbank.user.fearture.event.RegistrationCompleteEvent;
import com.apdbank.user.fearture.mail.MailService;
import com.apdbank.user.fearture.mail.VerificationToken.VerificationToken;
import com.apdbank.user.fearture.mail.VerificationToken.VerificationTokenRepository;
import com.apdbank.user.fearture.user.dto.*;
import com.apdbank.user.mapper.UserMapper;
import com.apdbank.user.utils.Utils;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    // TODO: create user

    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;


    @GetMapping
    ResponseEntity<?> getUserList(
            @RequestParam(required = false,defaultValue = "0")int page,
            @RequestParam(required = false,defaultValue = "12") int size
    ){

        return ResponseEntity.accepted().body(
                Map.of(
                        "users",userService.getUserList(page,size)
                )
        );
    }

    @GetMapping("/name/{username}")
    UserDetailResponse findUserByName(@PathVariable String username){
        log.info("Received User by UserName: {}", username);
        return userService.findUserByName(username);
    }

    @GetMapping("/{uuid}")
    UserDetailResponse findUserByUuid(@PathVariable String uuid){
        log.info("Received User by UUID: {}", uuid);
        return userService.findUserByUuid(uuid);
    }


    @PostMapping("/user-registration")
    public Object createUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest,
                             HttpServletRequest httpServletRequest
                            ) {

        // TODO: create user response
        UserResponse userResponse = userService.registerUser(userRegistrationRequest);

        // TODO: response
        User user = userMapper.formUserResponse(userResponse);
        user.setPassword(passwordEncoder.encode(userRegistrationRequest.password()));
        user.setConfirmPassword(passwordEncoder.encode(userRegistrationRequest.confirmPassword()));

       // TODO:  add role for user default
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("USER").orElseThrow(
                () -> new RuntimeException("User Role has been Not Found")
        ));

        // TODO: add default roles
        user.setRoles(roles);

        // TODO: save to database
        userRepository.save(user);

        log.info("User created: " + user.getUsername());

        publishRegistrationEvent(user, httpServletRequest);

        return ResponseEntity.ok(BasedMessage.builder()
                .message("User has been registered. Please verify your email address")
                .build());
    }

    private void publishRegistrationEvent(User user, ServletRequest servletRequest) {

        // TODO: Implement event publishing logic
        try{
            eventPublisher.publishEvent(new RegistrationCompleteEvent(user, Utils.getApplicationUrl((HttpServletRequest) servletRequest)));

        }catch (Exception e){
            log.error("Failed to publish registration event", e);
        }
        log.info("Publishing registration event for user: {}", user.getUsername());
    }

    @PostMapping("/verify-email")
    public UserRegistrationResponse verifyEmail(@RequestParam String token) {

        VerificationToken verificationToken = verificationTokenRepository.findByTokenAndType(token, VerificationToken.TokenType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token has not been found"));

        User user = verificationToken.getUser();

        String validatedToken = userService.validateVerificationToken(verificationToken.getToken());

        if (validatedToken.equals("valid")) {

            // TODO: unlock user after the user verify   the email
            user.setIsEmailVerified(true);
            user.setBlocked(false);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);

            // TODO: save user
            userRepository.save(user);

            // TODO: send welcome messages
            mailService.welcome(user);

            return UserRegistrationResponse.builder()
                    .message("Registration successfully")
                    .user(userMapper.toUserDetail(user))
                    .code(200)
                    .status(true)
                    .timeStamp(LocalDateTime.now())
                    .data(user.getEmail())
                    .token(verificationToken.getToken())
                    .build();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Token has not been found"
        );
    }



    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    void deleteUserByUuid(@PathVariable String uuid) {
        userService.deleteUserByUuid(uuid);
    }

    @PutMapping("/enable/{uuid}")
    BasedMessage enableUser(@PathVariable String uuid) {
        return userService.enableByUuid(uuid);
    }

    @PutMapping("/disable/{uuid}")
    BasedMessage disableUser(@PathVariable String uuid) {
        return userService.disableByUuid(uuid);
    }

    @PutMapping("/block/{uuid}")
    BasedMessage blockUser(@PathVariable String uuid) {
        return userService.blockUserByUuid(uuid);
    }

    //TODO: Update User By UUID
    @PutMapping("/resetPassword/{userName}")
    BasedMessage resetUserPassword(@Valid @PathVariable String userName, @RequestBody ResetPasswordRequest resetPasswordRequest){
        return userService.resetUserPassword(userName,resetPasswordRequest);
    }







}