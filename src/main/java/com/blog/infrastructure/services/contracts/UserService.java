package com.blog.infrastructure.services.contracts;

import com.blog.api.models.requests.UserRequest;
import com.blog.api.models.responses.UserResponse;
import com.blog.util.enums.SortType;
import org.springframework.data.domain.Page;

/**
 * @author claudio.vilas
 * date 09/2023
 */

public interface UserService {

    UserResponse getById(Long id);
    Page<UserResponse> getAll(int page, int size, SortType sortType);
    UserResponse update(Long id, UserRequest request);
    void delete(Long id);
}
