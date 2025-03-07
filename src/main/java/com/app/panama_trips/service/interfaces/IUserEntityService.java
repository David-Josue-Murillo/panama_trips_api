package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.presentation.dto.AuthCreateUserRequest;
import org.springframework.data.domain.Page;

public interface IUserEntityService {
    Page<UserEntity> getAllUser(Integer page, Integer size, Boolean enabledPagination);
    UserEntity getUserById(Long id);
    UserEntity saveUser(AuthCreateUserRequest authCreateUserRequest);
    UserEntity updateUser(Long id, AuthCreateUserRequest authCreateUserRequest);
    void deleteUser(Long id);
}
