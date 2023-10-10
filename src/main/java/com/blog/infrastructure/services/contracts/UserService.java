package com.blog.infrastructure.services.contracts;

import com.blog.api.models.requests.UserRequest;
import com.blog.api.models.responses.UserResponse;

/**
 * @author claudio.vilas
 * date 09/2023
 */

public interface UserService {

    UserResponse getById(Long id);
}
