package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.UserRequest;
import com.app.panama_trips.presentation.dto.UserResponse;
import org.springframework.data.domain.Page;

public interface IUserEntityService {
    Page<UserResponse> getAllUser(Integer page, Integer size, Boolean enabledPagination);
    UserResponse getUserById(Long id);
    UserResponse saveUser(UserRequest userRequest);
    UserResponse updateUser(Long id, UserRequest userRequest);
    void deleteUser(Long id);
}
