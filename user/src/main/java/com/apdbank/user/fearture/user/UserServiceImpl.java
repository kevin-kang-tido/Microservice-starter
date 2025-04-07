package com.apdbank.user.fearture.user;

import com.apdbank.user.base.BasedMessage;
import com.apdbank.user.domain.User;
import com.apdbank.user.fearture.mail.VerificationToken.VerificationToken;
import com.apdbank.user.fearture.mail.VerificationToken.VerificationTokenRepository;
import com.apdbank.user.fearture.user.dto.ResetPasswordRequest;
import com.apdbank.user.fearture.user.dto.UserDetailResponse;
import com.apdbank.user.fearture.user.dto.UserRegistrationRequest;
import com.apdbank.user.fearture.user.dto.UserResponse;
import com.apdbank.user.mapper.UserMapper;
import com.apdbank.user.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService  {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;

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
        if (userRegistrationRequest.password() == null || userRegistrationRequest.password().isEmpty()) {
            throw  new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password cannot be empty"
            );
        }

        // TODO: validate the password and confirm Password must match
        if (!userRegistrationRequest.password().equals(userRegistrationRequest.confirmPassword())){
            throw  new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Passwords and Confirm passwords  is not Match"
            );
        }


        // TODO: logs commfirm password
        log.info("Here is the confirm password: "+userRegistrationRequest.confirmPassword());


        user.setPassword(passwordEncoder.encode(userRegistrationRequest.password()));
        user.setUuid(Utils.generateRandomUUID());
        user.setUsername(user.getFirstName()+" "+ user.getLastName());
        user.setBlocked(false);
        user.setAccountNonLocked(false);
        user.setBlocked(true);
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());

        return userMapper.toUserResponse(user);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return "Invalid token!";
        }

        if (verificationToken.isExpired()) {
            return "Token is already expired, please resend the token!";
        }

        if (verificationToken.getIsUsed()) {
            return "Token is already used";
        }

        verificationToken.setIsUsed(true);

        verificationTokenRepository.save(verificationToken);

        return "valid";
    }

    @Override
    public void saveUserVerificationToken(User theUser, String verificationToken, VerificationToken.TokenType tokenType) {

        VerificationToken verificationToken1 = new VerificationToken(verificationToken, VerificationToken.TokenType.EMAIL_VERIFICATION);

        verificationToken1.setUser(theUser);

        verificationTokenRepository.save(verificationToken1);
    }

    @Override
    public Page<UserDetailResponse> getUserList(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page,size);
        Page<User> users = userRepository.findAll(pageRequest);

        return users.map(userMapper::toUserDetail);
    }

    @Override
    public UserDetailResponse findUserByName(String username) {
        // TODO: find user
        if (!userRepository.existsByUsername(username)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has been not found");
        }

        // TODO:  find user by name
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User"+username+" has been  not found"));

        return userMapper.toUserDetail(user);
    }

    @Override
    public UserDetailResponse findUserByUuid(String uuid) {

        //TODO: find user
        if (!userRepository.existsByUuid(uuid)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has been not found"
            );
        }


        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has been not found"
                ));

        return userMapper.toUserDetail(user);
    }

    @Override
    public void deleteUserByUuid(String uuid) {
        // TODO: find user
        if (!userRepository.existsByUuid(uuid)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has been not found"
            );
        }


        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has been not found"
                ));

       userRepository.delete(user);

    }

    @Override
    public BasedMessage enableByUuid(String uuid) {
        // TODO: find user
        if (!userRepository.existsByUuid(uuid)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has been not found"
            );
        }
        userRepository.enableByUuid(uuid);

        return new BasedMessage("User already Enable");
    }

    @Override
    public BasedMessage disableByUuid(String uuid) {
        // TODO: find user
        if (!userRepository.existsByUuid(uuid)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has been not found"
            );
        }
        userRepository.enableByUuid(uuid);

        return new BasedMessage("User already Disable");
    }

    @Override
    public BasedMessage blockUserByUuid(String uuid) {
        // TODO: find user
        if (!userRepository.existsByUuid(uuid)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has been not found"
            );
        }
        userRepository.blockByUuid(uuid);

        return new BasedMessage("User already Blocked");
    }

    @Override
    public BasedMessage resetUserPassword(String userName, ResetPasswordRequest resetPasswordRequest) {

        log.info("User edit Password : "+resetPasswordRequest);


        // TODO: password and confirm password must match
        if (!resetPasswordRequest.newPassword().equals(resetPasswordRequest.confirmedPassword())){
            throw  new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Password is not match! Please Try again"
            );
        }
        User user = userRepository.findUserByUsername(userName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));

        userRepository.save(user);

        return new BasedMessage(" Here is your update Password : "+resetPasswordRequest.newPassword());
    }


}
