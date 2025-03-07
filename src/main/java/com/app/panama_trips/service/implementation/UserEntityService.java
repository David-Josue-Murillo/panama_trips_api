package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.UserNotFoundException;
import com.app.panama_trips.exception.ValidationException;
import com.app.panama_trips.persistence.entity.RoleEntity;
import com.app.panama_trips.persistence.entity.RoleEnum;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.RoleRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.AuthCreateUserRequest;
import com.app.panama_trips.service.interfaces.IUserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserEntityService implements IUserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final RoleRepository roleRepository;

    private void validateUser(AuthCreateUserRequest authCreateUserRequest) {
        if(authCreateUserRequest.password().length() < 5) {
            throw new ValidationException("Password must be at least 4 characters long");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserEntity> getAllUser(Integer page, Integer size, Boolean enabledPagination) {
        Pageable pageable = enabledPagination
                ? PageRequest.of(page, size)
                : Pageable.unpaged();
        return userEntityRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserById(Long id) {
        return userEntityRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public UserEntity saveUser(AuthCreateUserRequest authCreateUserRequest) {
        validateUser(authCreateUserRequest);

        UserEntity userEntity = new UserEntity();
        userEntity.setName(authCreateUserRequest.name());
        userEntity.setLastname(authCreateUserRequest.lastname());
        userEntity.setEmail(authCreateUserRequest.email());
        userEntity.setDni(authCreateUserRequest.dni());
        userEntity.setPasswordHash(authCreateUserRequest.password());
        userEntity.setRole_id(this.roleRepository.findByRoleEnum(RoleEnum.CUSTOMER));
        return userEntityRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity updateUser(Long id, AuthCreateUserRequest authCreateUserRequest) {
        validateUser(authCreateUserRequest);
        UserEntity existingUser = userEntityRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        existingUser.setDni(authCreateUserRequest.dni());
        existingUser.setName(authCreateUserRequest.name());
        existingUser.setLastname(authCreateUserRequest.lastname());
        existingUser.setEmail(authCreateUserRequest.email());
        existingUser.setPasswordHash(authCreateUserRequest.password());

        return userEntityRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        UserEntity existingUser = userEntityRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userEntityRepository.delete(existingUser);
    }
}
