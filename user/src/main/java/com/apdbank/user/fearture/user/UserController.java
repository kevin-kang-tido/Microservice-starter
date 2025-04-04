package com.apdbank.user.fearture.user;

import com.apdbank.user.base.BasedMessage;
import com.apdbank.user.domain.Role;
import com.apdbank.user.domain.User;
import com.apdbank.user.fearture.event.RegistrationCompleteEvent;
import com.apdbank.user.fearture.user.dto.UserRegistrationRequest;
import com.apdbank.user.fearture.user.dto.UserResponse;
import com.apdbank.user.mapper.UserMapper;
import com.apdbank.user.utils.Utils;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
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

    @PostMapping("/create")
    public Object createUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest,
                             HttpServletRequest httpServletRequest) {

     // TODO: create user response
        UserResponse userResponse = userService.registerUser(userRegistrationRequest);

        // TODO: response
        User user = userMapper.toUserResponse(userResponse);

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
}