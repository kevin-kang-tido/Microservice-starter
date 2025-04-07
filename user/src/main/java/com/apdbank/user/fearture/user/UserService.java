package com.apdbank.user.fearture.user;
import com.apdbank.user.base.BasedMessage;
import com.apdbank.user.domain.User;
import com.apdbank.user.fearture.mail.VerificationToken.VerificationToken;
import com.apdbank.user.fearture.user.dto.ResetPasswordRequest;
import com.apdbank.user.fearture.user.dto.UserDetailResponse;
import com.apdbank.user.fearture.user.dto.UserRegistrationRequest;
import com.apdbank.user.fearture.user.dto.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService  {

    UserResponse registerUser(UserRegistrationRequest userRegistrationRequest);

//    void sendTokenForEmailVerification(User user, String verificationToken);

     String validateVerificationToken(String token);

    void saveUserVerificationToken(User theUser, String verificationToken, VerificationToken.TokenType tokenType);

    // TODO: get get  List User
    Page<UserDetailResponse> getUserList(int page, int size);

    UserDetailResponse findUserByName(String username);

    UserDetailResponse findUserByUuid(String uuid);

    void deleteUserByUuid(String uuid);


    BasedMessage enableByUuid(String uuid);

    BasedMessage disableByUuid(String uuid);

    BasedMessage blockUserByUuid(String uuid);

    // update User  Password
    // TODO: admin Reset Password
    BasedMessage resetUserPassword(String userName, ResetPasswordRequest  resetPasswordRequest);
}

