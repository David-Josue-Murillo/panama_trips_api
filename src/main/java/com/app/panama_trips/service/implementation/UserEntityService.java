package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.UserNotFoundException;
import com.app.panama_trips.exception.ValidationException;
import com.app.panama_trips.persistence.entity.RoleEnum;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.RoleRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.UserRequest;
import com.app.panama_trips.presentation.dto.UserResponse;
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

    private void validateUser(String email) {
        if(this.userEntityRepository.findUserEntitiesByEmail(email).isPresent()) {
            throw new ValidationException("Email already exists: " + email);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUser(Integer page, Integer size, Boolean enabledPagination) {
        Pageable pageable = enabledPagination
                ? PageRequest.of(page, size)
                : Pageable.unpaged();
        return userEntityRepository.findAll(pageable).map(UserResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return userEntityRepository.findById(id)
                .map(UserResponse::new)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public UserResponse saveUser(UserRequest userRequest) {
        validateUser(userRequest.email());
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRequest.name());
        userEntity.setLastname(userRequest.lastname());
        userEntity.setEmail(userRequest.email());
        userEntity.setDni(userRequest.dni());
        userEntity.setPasswordHash(userRequest.password());
        userEntity.setProfileImageUrl(userRequest.profileImageUrl());
        userEntity.setRole_id(this.roleRepository.findByRoleEnum(RoleEnum.CUSTOMER));
        return new UserResponse((userEntityRepository.save(userEntity)));
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        UserEntity existingUser = userEntityRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        existingUser.setDni(userRequest.dni());
        existingUser.setName(userRequest.name());
        existingUser.setLastname(userRequest.lastname());
        existingUser.setEmail(userRequest.email());
        existingUser.setPasswordHash(userRequest.password());
        existingUser.setProfileImageUrl(userRequest.profileImageUrl());

        return new UserResponse(userEntityRepository.save(existingUser));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        UserEntity existingUser = userEntityRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userEntityRepository.delete(existingUser);
    }
}
