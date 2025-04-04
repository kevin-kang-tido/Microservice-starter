package com.apdbank.user.fearture.user;

import com.apdbank.user.domain.User;
import com.apdbank.user.fearture.user.dto.UserRegistrationRequest;
import com.apdbank.user.fearture.user.dto.UserResponse;
import com.apdbank.user.mapper.UserMapper;
import com.apdbank.user.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService  {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(UserRegistrationRequest userRegistrationRequest) {

        // TODO:: covert request to dto

        // TODO: the email is already exit or not
        User user = userMapper.fromUserCreateRequest(userRegistrationRequest);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw  new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email address already in use"
            );
        }

        // TODO: username is ready exist or not
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username already in use"
            );
        }

        // TODO: validate the password this not null
        if (userRegistrationRequest.password() == null || !userRegistrationRequest.password().isEmpty()) {
            throw  new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password cannot be empty"
            );
        }

        // TODO: validate the password and comfrim Password must match
        if (!userRegistrationRequest.password().equals(userRegistrationRequest.comfirmPassword())){
            throw  new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Passwords and Confirm passwords  is not Match"
            );
        }

        user.setUuid(Utils.generateRandomUUID());
        user.setBlocked(false);
        user.setEmailVerified(false);
        user.setPassword(passwordEncoder.encode(userRegistrationRequest.password()));

        return userMapper.toUserResponse(user);
    }
}
