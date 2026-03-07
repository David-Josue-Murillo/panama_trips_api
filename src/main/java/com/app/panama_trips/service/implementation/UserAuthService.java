package com.app.panama_trips.service.implementation;

import com.app.panama_trips.persistence.entity.RoleEnum;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.RoleRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.AuthCreateUserRequest;
import com.app.panama_trips.presentation.dto.AuthLoginRequest;
import com.app.panama_trips.presentation.dto.AuthResponse;
import com.app.panama_trips.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailServiceImpl userDetailService;
    private final UserEntityRepository userEntityRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public AuthResponse login (AuthLoginRequest authLoginRequest) {

        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authentication(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long userId = this.userEntityRepository.findUserEntitiesByName(username)
                .orElseThrow(() -> new BadCredentialsException("User not found"))
                .getId();

        String token = this.jwtUtil.generateToken(authentication, userId);
        return new AuthResponse(username, "Logged in successfully", token, true);
    }

    @Transactional
    public AuthResponse create(AuthCreateUserRequest authCreateUserRequest) {
        String name = authCreateUserRequest.name();
        String lastname = authCreateUserRequest.lastname();
        String dni = authCreateUserRequest.dni();
        String email = authCreateUserRequest.email();
        String password = authCreateUserRequest.password();
        String role = RoleEnum.CUSTOMER.name();

        // Check if the user already exists
        if(this.userEntityRepository.findUserEntitiesByEmail(email).isPresent()) {
            throw new BadCredentialsException("Email already exists");
        }

        // Create the user
        UserEntity userEntity = UserEntity.builder()
                .name(name)
                .lastname(lastname)
                .dni(dni)
                .email(email)
                .passwordHash(this.passwordEncoder.encode(password))
                .role(this.roleRepository.findByRoleEnum(RoleEnum.valueOf(role)))
                .build();

        UserEntity savedUser = this.userEntityRepository.save(userEntity);

        // Creating the authorities for the user
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role)));
        savedUser.getRole().getPermissions()
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getPermissionEnum().name())));

        // Create the authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getName(), savedUser.getPasswordHash(), authorities);
        String token = this.jwtUtil.generateToken(authentication, savedUser.getId());

        return new AuthResponse(savedUser.getName(), "User created successfully", token, true);
    }

    private Authentication authentication(String username, String password) {
        UserDetails userDetails = this.userDetailService.loadUserByUsername(username);

        if(userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if(!this.passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
