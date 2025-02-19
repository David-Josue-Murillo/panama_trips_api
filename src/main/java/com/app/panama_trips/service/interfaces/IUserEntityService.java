package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;

public interface IUserEntityService {
    Page<UserEntity> getAllUser(Integer page, Integer size, Boolean enabledPagination);
    UserEntity getUSerById(Long id);
    UserEntity saveUser(UserEntity userEntity);
    UserEntity updateUser(Long id, UserEntity userEntity);
    void deleteUser(Long id);
}
