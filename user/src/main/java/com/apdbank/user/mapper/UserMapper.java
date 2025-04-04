package com.apdbank.user.mapper;

import com.apdbank.user.domain.User;
import com.apdbank.user.fearture.user.dto.UserDetailResponse;
import com.apdbank.user.fearture.user.dto.UserRegistrationRequest;
import com.apdbank.user.fearture.user.dto.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDetailResponse toUserDetailResponse(User user);


    User toUserResponse(UserResponse userResponse);


    User fromUserCreateRequest(UserRegistrationRequest userRegistrationRequest);
}
