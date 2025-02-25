package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.UserNotFoundException;
import com.app.panama_trips.exception.ValidationException;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.service.interfaces.IUserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserEntityService implements IUserEntityService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    private void validateUser(UserEntity userEntity) {
        List<String> errors = new ArrayList<>();

        if(userEntity.getName() == null || userEntity.getName().trim().isEmpty()){
            errors.add("Username is required");
        }

        if(userEntity.getPasswordHash() == null || userEntity.getPasswordHash().trim().isEmpty()){
            errors.add("Password is required");
        }

        if(userEntity.getPasswordHash().length() < 4) {
            errors.add("Password must be at least 4 characters long");
        }

        if(!errors.isEmpty()){
            throw new ValidationException("User validation failed", errors);
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
    public UserEntity saveUser(UserEntity userEntity) {
        validateUser(userEntity);
        return userEntityRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity updateUser(Long id, UserEntity userEntity) {
        validateUser(userEntity);
        UserEntity existingUser = userEntityRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        existingUser.setDni(userEntity.getDni());
        existingUser.setName(userEntity.getName());
        existingUser.setLastname(userEntity.getLastname());
        existingUser.setEmail(userEntity.getEmail());
        existingUser.setPasswordHash(userEntity.getPasswordHash());
        existingUser.setProfileImageUrl(userEntity.getProfileImageUrl());
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
