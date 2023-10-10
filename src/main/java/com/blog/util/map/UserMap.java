package com.blog.util.map;

import com.blog.api.models.responses.UserResponse;
import com.blog.domain.entities.ApplicationUser;

/**
 * @author claudio.vilas
 * date 09/2023
 */

public class UserMap {
    public static UserResponse mapToDto(ApplicationUser user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(user.getAuthorities())
                .createAt(user.getCreateAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
