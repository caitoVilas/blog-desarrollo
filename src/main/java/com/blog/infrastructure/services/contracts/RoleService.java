package com.blog.infrastructure.services.contracts;

import com.blog.api.models.responses.UserResponse;

/**
 * @author claudio.vilas
 * date 10/2023
 */

public interface RoleService {
    UserResponse changeRoleAdmin(Long userId);
    UserResponse changeRoleUser(Long userId);

}
