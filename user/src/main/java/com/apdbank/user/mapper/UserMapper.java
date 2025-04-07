package com.apdbank.user.mapper;

import com.apdbank.user.domain.User;
import com.apdbank.user.fearture.user.dto.ResetPasswordRequest;
import com.apdbank.user.fearture.user.dto.UserDetailResponse;
import com.apdbank.user.fearture.user.dto.UserRegistrationRequest;
import com.apdbank.user.fearture.user.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDetailResponse toUserDetailResponse(User user);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "blocked", target = "isBlocked")
    @Mapping(source = "isEmailVerified", target = "isEmailVerified")
    @Mapping(source = "accountNonExpired", target = "isAccountNonExpired")
    @Mapping(source = "accountNonLocked", target = "isAccountNonLocked")
    @Mapping(source = "credentialsNonExpired", target = "isCredentialsNonExpired")
    @Mapping(source = "roles", target = "roles")
    UserDetailResponse toUserDetail(User user);

    UserResponse toUserResponse(User user);

    User formUserResponse(UserResponse userResponse);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phoneNumber",target = "phoneNumber")
//    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "confirmPassword",target = "confirmPassword")
    User fromUserCreateRequest(UserRegistrationRequest userRegistrationRequest);


//    @Mapping(source = "newPassword", target = "newPassword")
//    @Mapping(source = "confirmPassword",target = "confirmPassword")
//    User formUserResetPasswordRequest(ResetPasswordRequest forgetPasswordRequest);

}
