package com.app.panama_trips.service.implementation;

import com.app.panama_trips.persistence.entity.RoleEnum;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.AuthCreateUserRequest;
import com.app.panama_trips.presentation.dto.AuthLoginRequest;
import com.app.panama_trips.presentation.dto.AuthResponse;
import com.app.panama_trips.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class UserAuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    public AuthResponse login (AuthLoginRequest authLoginRequest) {

        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authentication(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = this.jwtUtil.generateToken(authentication);
        return new AuthResponse(username, "Logged in successfully", token, true);
    }

    public AuthResponse create(AuthCreateUserRequest authCreateUserRequest) {
        // Get dara from the request
        String name = authCreateUserRequest.name();
        String lastname = authCreateUserRequest.lastname();
        String dni = authCreateUserRequest.dni();
        String email = authCreateUserRequest.email();
        String password = authCreateUserRequest.password();
        String role = RoleEnum.CUSTOMER.name();

        // Validate the user
        this.validateUser(name, email, password);

        // Check if the user already exists
        if(this.userEntityRepository.findUserEntitiesByEmail(email)) {
            throw new BadCredentialsException("Email already exists");
        }

        // Create the user
        UserEntity userEntity = UserEntity.builder()
                .name(name)
                .lastname(lastname)
                .dni(dni)
                .email(email)
                .passwordHash(password)
                .build();

        UserEntity savedUser = this.userEntityRepository.save(userEntity);

        // Creating the authorities for the user
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role)));
        savedUser.getRole_id().getPermissions()
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getPermissionEnum().name())));

        // Create the authentication
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getName(), savedUser.getPasswordHash(), authorities);
        String token = this.jwtUtil.generateToken(authentication);

        return new AuthResponse(savedUser.getName(), "User created successfully", token, true);
    }

    private void validateUser(String username, String email, String password) {
        if(username == null || username.trim().isEmpty()) {
            throw new BadCredentialsException("Username is required");
        }

        if(email == null || email.trim().isEmpty()) {
            throw new BadCredentialsException("Email is required");
        }

        if(password == null || password.trim().isEmpty()) {
            throw new BadCredentialsException("Password is required");
        }

        if(password.length() < 6) {
            throw new BadCredentialsException("Password must be at least 6 characters long");
        }
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
