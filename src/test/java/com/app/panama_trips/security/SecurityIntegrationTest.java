package com.app.panama_trips.security;

import com.app.panama_trips.persistence.entity.PermissionEntity;
import com.app.panama_trips.persistence.entity.PermissionEnum;
import com.app.panama_trips.persistence.entity.RoleEntity;
import com.app.panama_trips.persistence.entity.RoleEnum;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.RoleRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.AuthCreateUserRequest;
import com.app.panama_trips.presentation.dto.AuthLoginRequest;
import com.app.panama_trips.utility.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private RoleEntity adminRole;
    private RoleEntity customerRole;

    @BeforeEach
    void setUp() {
        userEntityRepository.deleteAll();
        roleRepository.deleteAll();

        PermissionEntity readPermission = PermissionEntity.builder()
                .permissionEnum(PermissionEnum.USER_READ)
                .build();
        PermissionEntity createPermission = PermissionEntity.builder()
                .permissionEnum(PermissionEnum.USER_CREATE)
                .build();

        adminRole = roleRepository.save(RoleEntity.builder()
                .roleEnum(RoleEnum.ADMIN)
                .permissions(Set.of(readPermission, createPermission))
                .build());

        customerRole = roleRepository.save(RoleEntity.builder()
                .roleEnum(RoleEnum.CUSTOMER)
                .permissions(Set.of(readPermission))
                .build());
    }

    @Nested
    @DisplayName("Authentication Tests - Login")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully with valid credentials")
        void login_shouldReturnToken_whenCredentialsAreValid() throws Exception {
            UserEntity user = createUser("testuser", "test@example.com", "Password123!", customerRole);

            AuthLoginRequest loginRequest = new AuthLoginRequest("testuser", "Password123!");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("testuser"))
                    .andExpect(jsonPath("$.jwt").isNotEmpty())
                    .andExpect(jsonPath("$.status").value(true))
                    .andExpect(jsonPath("$.message").value("Logged in successfully"));
        }

        @Test
        @DisplayName("Should return error when username does not exist")
        void login_shouldReturnError_whenUserDoesNotExist() throws Exception {
            AuthLoginRequest loginRequest = new AuthLoginRequest("nonexistent", "Password123!");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("Should return 401 when password is incorrect")
        void login_shouldReturnUnauthorized_whenPasswordIsIncorrect() throws Exception {
            createUser("testuser", "test@example.com", "Password123!", customerRole);

            AuthLoginRequest loginRequest = new AuthLoginRequest("testuser", "wrongpassword");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.message").value("Invalid password"));
        }

        @Test
        @DisplayName("Should return 400 when username is blank")
        void login_shouldReturnBadRequest_whenUsernameIsBlank() throws Exception {
            AuthLoginRequest loginRequest = new AuthLoginRequest("", "Password123!");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 when password is too short")
        void login_shouldReturnBadRequest_whenPasswordIsTooShort() throws Exception {
            AuthLoginRequest loginRequest = new AuthLoginRequest("testuser", "12345");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Authentication Tests - Registration")
    class RegistrationTests {

        @Test
        @DisplayName("Should register user successfully with valid data")
        void register_shouldCreateUser_whenDataIsValid() throws Exception {
            AuthCreateUserRequest request = new AuthCreateUserRequest(
                    "John",
                    "Doe",
                    "8-123-456789",
                    "john.doe@example.com",
                    "Password123!"
            );

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username").value("John"))
                    .andExpect(jsonPath("$.jwt").isNotEmpty())
                    .andExpect(jsonPath("$.status").value(true))
                    .andExpect(jsonPath("$.message").value("User created successfully"));

            assertTrue(userEntityRepository.findUserEntitiesByEmail("john.doe@example.com").isPresent());
        }

        @Test
        @DisplayName("Should return error when email already exists")
        void register_shouldReturnError_whenEmailExists() throws Exception {
            AuthCreateUserRequest firstRequest = new AuthCreateUserRequest(
                    "Existing",
                    "User",
                    "8-123-456788",
                    "existing@example.com",
                    "Password123!"
            );

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(firstRequest)))
                    .andExpect(status().isCreated());

            AuthCreateUserRequest duplicateRequest = new AuthCreateUserRequest(
                    "John",
                    "Doe",
                    "8-123-456789",
                    "existing@example.com",
                    "Password123!"
            );

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(duplicateRequest)))
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.message").value("Email already exists"));
        }

        @Test
        @DisplayName("Should return 400 when name contains invalid characters")
        void register_shouldReturnBadRequest_whenNameHasInvalidCharacters() throws Exception {
            AuthCreateUserRequest request = new AuthCreateUserRequest(
                    "John123",
                    "Doe",
                    "8-123-456789",
                    "john.doe@example.com",
                    "Password123!"
            );

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 when DNI format is invalid")
        void register_shouldReturnBadRequest_whenDniFormatIsInvalid() throws Exception {
            AuthCreateUserRequest request = new AuthCreateUserRequest(
                    "John",
                    "Doe",
                    "12345678",
                    "john.doe@example.com",
                    "Password123!"
            );

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 when email format is invalid")
        void register_shouldReturnBadRequest_whenEmailFormatIsInvalid() throws Exception {
            AuthCreateUserRequest request = new AuthCreateUserRequest(
                    "John",
                    "Doe",
                    "8-123-456789",
                    "invalid-email",
                    "Password123!"
            );

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 when password is too short")
        void register_shouldReturnBadRequest_whenPasswordIsTooShort() throws Exception {
            AuthCreateUserRequest request = new AuthCreateUserRequest(
                    "John",
                    "Doe",
                    "8-123-456789",
                    "john.doe@example.com",
                    "12345"
            );

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Authorization Tests - Protected Endpoints")
    class AuthorizationTests {

        @Test
        @DisplayName("Should return 403 when accessing protected endpoint without token")
        void protectedEndpoint_shouldReturn403_whenNoToken() throws Exception {
            mockMvc.perform(get("/api/user"))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return 403 when token format is incorrect")
        void protectedEndpoint_shouldReturn403_whenTokenFormatIsIncorrect() throws Exception {
            mockMvc.perform(get("/api/user")
                            .header("Authorization", "InvalidFormat token"))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return 403 when CUSTOMER tries to access ADMIN endpoint")
        void adminEndpoint_shouldReturn403_whenUserIsCustomer() throws Exception {
            String token = generateTokenForRole(RoleEnum.CUSTOMER, "customer");

            mockMvc.perform(get("/api/user")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should allow ADMIN to access ADMIN endpoint")
        void adminEndpoint_shouldAllow_whenUserIsAdmin() throws Exception {
            String token = generateTokenForRole(RoleEnum.ADMIN, "admin");

            mockMvc.perform(get("/api/user")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return 403 when CUSTOMER tries to access provinces endpoint")
        void provincesEndpoint_shouldReturn403_whenUserIsCustomer() throws Exception {
            String token = generateTokenForRole(RoleEnum.CUSTOMER, "customer");

            mockMvc.perform(get("/api/provinces")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return 403 when CUSTOMER tries to access districts endpoint")
        void districtsEndpoint_shouldReturn403_whenUserIsCustomer() throws Exception {
            String token = generateTokenForRole(RoleEnum.CUSTOMER, "customer");

            mockMvc.perform(get("/api/districts")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should throw exception when accessing protected endpoint with empty Bearer token")
        void protectedEndpoint_shouldThrowException_whenBearerTokenIsEmpty() {
            assertThrows(Exception.class, () ->
                    mockMvc.perform(get("/api/user")
                            .header("Authorization", "Bearer "))
            );
        }

        @Test
        @DisplayName("Should return 403 when accessing endpoint with only Bearer keyword")
        void protectedEndpoint_shouldReturn403_whenOnlyBearerKeyword() throws Exception {
            mockMvc.perform(get("/api/user")
                            .header("Authorization", "Bearer"))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Public Endpoints Tests")
    class PublicEndpointsTests {

        @Test
        @DisplayName("Should allow access to login endpoint without token")
        void loginEndpoint_shouldBeAccessible_withoutToken() throws Exception {
            createUser("accessibleuser", "accessible@example.com", "Password123!", customerRole);

            AuthLoginRequest loginRequest = new AuthLoginRequest("accessibleuser", "Password123!");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should allow access to signup endpoint without token")
        void signupEndpoint_shouldBeAccessible_withoutToken() throws Exception {
            AuthCreateUserRequest request = new AuthCreateUserRequest(
                    "Test",
                    "User",
                    "8-111-111111",
                    "test.user@example.com",
                    "Password123!"
            );

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should allow access to swagger-ui without token")
        void swaggerUi_shouldBeAccessible_withoutToken() throws Exception {
            mockMvc.perform(get("/swagger-ui/index.html"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should allow access to api-docs without token")
        void apiDocs_shouldBeAccessible_withoutToken() throws Exception {
            mockMvc.perform(get("/v3/api-docs"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("JWT Token Tests")
    class JwtTokenTests {

        @Test
        @DisplayName("Should generate valid token on login")
        void login_shouldGenerateValidToken() throws Exception {
            createUser("testuser", "test@example.com", "Password123!", customerRole);

            AuthLoginRequest loginRequest = new AuthLoginRequest("testuser", "Password123!");

            MvcResult result = mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andReturn();

            String responseBody = result.getResponse().getContentAsString();
            String token = objectMapper.readTree(responseBody).get("jwt").asText();

            assertNotNull(token);
            assertFalse(token.isEmpty());
            assertDoesNotThrow(() -> jwtUtil.validateToken(token));
        }

        @Test
        @DisplayName("Should include correct username in token")
        void login_tokenShouldContainCorrectUsername() throws Exception {
            createUser("testuser", "test@example.com", "Password123!", customerRole);

            AuthLoginRequest loginRequest = new AuthLoginRequest("testuser", "Password123!");

            MvcResult result = mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andReturn();

            String responseBody = result.getResponse().getContentAsString();
            String token = objectMapper.readTree(responseBody).get("jwt").asText();

            var decodedJWT = jwtUtil.validateToken(token);
            assertEquals("testuser", jwtUtil.getUsernameFromToken(decodedJWT));
        }

        @Test
        @DisplayName("Should include authorities in token")
        void login_tokenShouldContainAuthorities() throws Exception {
            createUser("testuser", "test@example.com", "Password123!", customerRole);

            AuthLoginRequest loginRequest = new AuthLoginRequest("testuser", "Password123!");

            MvcResult result = mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andReturn();

            String responseBody = result.getResponse().getContentAsString();
            String token = objectMapper.readTree(responseBody).get("jwt").asText();

            var decodedJWT = jwtUtil.validateToken(token);
            String authorities = jwtUtil.getSpecificClaim(decodedJWT, "authorities").asString();

            assertNotNull(authorities);
            assertTrue(authorities.contains("ROLE_CUSTOMER"));
        }
    }

    @Nested
    @DisplayName("End-to-End Flow Tests")
    class EndToEndFlowTests {

        @Test
        @DisplayName("Should complete full registration and login flow")
        void fullFlow_registerThenLogin_shouldSucceed() throws Exception {
            AuthCreateUserRequest registerRequest = new AuthCreateUserRequest(
                    "NewUser",
                    "Test",
                    "8-999-999999",
                    "newuser@example.com",
                    "Password123!"
            );

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerRequest)))
                    .andExpect(status().isCreated());

            AuthLoginRequest loginRequest = new AuthLoginRequest("NewUser", "Password123!");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("NewUser"))
                    .andExpect(jsonPath("$.jwt").isNotEmpty());
        }

        @Test
        @DisplayName("Should access protected endpoint with token from login")
        void fullFlow_loginAndAccessProtectedEndpoint_shouldSucceed() throws Exception {
            createUser("adminuser", "admin@example.com", "Password123!", adminRole);

            AuthLoginRequest loginRequest = new AuthLoginRequest("adminuser", "Password123!");

            MvcResult loginResult = mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andReturn();

            String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                    .get("jwt").asText();

            mockMvc.perform(get("/api/user")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should deny access when using token from CUSTOMER for ADMIN endpoint")
        void fullFlow_customerTokenForAdminEndpoint_shouldFail() throws Exception {
            createUser("customeruser", "customer@example.com", "Password123!", customerRole);

            AuthLoginRequest loginRequest = new AuthLoginRequest("customeruser", "Password123!");

            MvcResult loginResult = mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andReturn();

            String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                    .get("jwt").asText();

            mockMvc.perform(get("/api/user")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Security Headers Tests")
    class SecurityHeadersTests {

        @Test
        @DisplayName("Should reject or error request without Content-Type header for POST")
        void postEndpoint_shouldRejectWithoutContentType() throws Exception {
            mockMvc.perform(post("/auth/login")
                            .content("{\"username\":\"test\",\"password\":\"Password123!\"}"))
                    .andExpect(status().is5xxServerError());
        }

        @Test
        @DisplayName("Should reject or error request with wrong Content-Type")
        void postEndpoint_shouldRejectWrongContentType() throws Exception {
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("{\"username\":\"test\",\"password\":\"Password123!\"}"))
                    .andExpect(status().is5xxServerError());
        }
    }

    @Nested
    @DisplayName("Role-Based Access Control Tests")
    class RoleBasedAccessControlTests {

        @Test
        @DisplayName("Should allow ADMIN to access all ADMIN-only endpoints")
        void adminRole_shouldAccessAllAdminEndpoints() throws Exception {
            String token = generateTokenForRole(RoleEnum.ADMIN, "admin");

            mockMvc.perform(get("/api/user")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/provinces")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/districts")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should deny CUSTOMER access to all ADMIN-only endpoints")
        void customerRole_shouldNotAccessAdminEndpoints() throws Exception {
            String token = generateTokenForRole(RoleEnum.CUSTOMER, "customer");

            mockMvc.perform(get("/api/user")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());

            mockMvc.perform(get("/api/provinces")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());

            mockMvc.perform(get("/api/districts")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());
        }
    }

    private UserEntity createUser(String name, String email, String password, RoleEntity role) {
        UserEntity user = UserEntity.builder()
                .name(name)
                .lastname("Test")
                .dni("8-000-000001")
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .role_id(role)
                .build();
        return userEntityRepository.save(user);
    }

    private String generateTokenForRole(RoleEnum roleEnum, String username) {
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + roleEnum.name())
        );
        var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
        return jwtUtil.generateToken(authentication);
    }
}
