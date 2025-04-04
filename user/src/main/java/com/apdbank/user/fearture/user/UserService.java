package com.apdbank.user.fearture.user;

import com.apdbank.user.fearture.user.dto.UserRegistrationRequest;
import com.apdbank.user.fearture.user.dto.UserDetailResponse;
import com.apdbank.user.fearture.user.dto.UserResponse;

public interface UserService  {

    UserResponse registerUser(UserRegistrationRequest userRegistrationRequest);

}
