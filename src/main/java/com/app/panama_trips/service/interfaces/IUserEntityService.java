package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.presentation.dto.AuthCreateUserRequest;
import com.app.panama_trips.presentation.dto.UserRequest;
import org.springframework.data.domain.Page;

public interface IUserEntityService {
    Page<UserEntity> getAllUser(Integer page, Integer size, Boolean enabledPagination);
    UserEntity getUserById(Long id);
    UserEntity saveUser(UserRequest userRequest);
    UserEntity updateUser(Long id, UserRequest userRequest);
    void deleteUser(Long id);
}
