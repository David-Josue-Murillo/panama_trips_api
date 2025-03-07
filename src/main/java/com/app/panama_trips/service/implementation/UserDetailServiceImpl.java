package com.app.panama_trips.service.implementation;

import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userEntityRepository.findUserEntitiesByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(userEntity.getRole_id().getRoleEnum().toString())));
        userEntity.getRole_id().getPermissions().stream()
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionEnum().toString())));

        return new User(
                userEntity.getName(),
                userEntity.getPasswordHash(),
                authorityList
        );
    }
}
