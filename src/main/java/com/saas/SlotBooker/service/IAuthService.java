package com.saas.SlotBooker.service;

import com.saas.SlotBooker.domain.auth.LoginRequest;
import com.saas.SlotBooker.domain.auth.LoginResponse;
import com.saas.SlotBooker.domain.auth.RegisterUserRequest;
import com.saas.SlotBooker.domain.auth.RegisterUserResponse;

public interface IAuthService {

    LoginResponse login(LoginRequest request);

    RegisterUserResponse registerUser(RegisterUserRequest request);
}
