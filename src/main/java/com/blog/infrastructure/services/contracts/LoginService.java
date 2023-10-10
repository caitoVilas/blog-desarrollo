package com.blog.infrastructure.services.contracts;

import com.blog.api.models.requests.LoginRequest;
import com.blog.api.models.requests.UserRequest;
import com.blog.api.models.responses.LoginResponse;
import com.blog.api.models.responses.UserResponse;

public interface LoginService {
    UserResponse create(UserRequest request);
    LoginResponse login(LoginRequest request);
}
