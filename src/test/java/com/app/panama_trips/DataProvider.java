package com.app.panama_trips;

import com.app.panama_trips.persistence.entity.*;
import com.app.panama_trips.presentation.dto.*;
import java.util.List;
import java.util.Set;

public class DataProvider {

    // PermissionEntity instances
    public static PermissionEntity USER_READ = PermissionEntity.builder()
            .id(1)
            .permissionEnum(PermissionEnum.USER_READ)
            .build();

    public static PermissionEntity USER_CREATE = PermissionEntity.builder()
            .id(2)
            .permissionEnum(PermissionEnum.USER_CREATE)
            .build();

    public static PermissionEntity USER_UPDATE = PermissionEntity.builder()
            .id(3)
            .permissionEnum(PermissionEnum.USER_UPDATE)
            .build();

    public static PermissionEntity USER_DELETE = PermissionEntity.builder()
            .id(4)
            .permissionEnum(PermissionEnum.USER_DELETE)
            .build();

    public static PermissionEntity BOOKING_READ = PermissionEntity.builder()
            .id(5)
            .permissionEnum(PermissionEnum.BOOKING_READ)
            .build();

    public static PermissionEntity BOOKING_CREATE = PermissionEntity.builder()
            .id(6)
            .permissionEnum(PermissionEnum.BOOKING_CREATE)
            .build();

    public static PermissionEntity BOOKING_UPDATE = PermissionEntity.builder()
            .id(7)
            .permissionEnum(PermissionEnum.BOOKING_UPDATE)
            .build();

    public static PermissionEntity BOOKING_DELETE = PermissionEntity.builder()
            .id(8)
            .permissionEnum(PermissionEnum.BOOKING_DELETE)
            .build();

    public static PermissionEntity PAYMENT_READ = PermissionEntity.builder()
            .id(9)
            .permissionEnum(PermissionEnum.PAYMENT_READ)
            .build();

    public static PermissionEntity PAYMENT_REFUND = PermissionEntity.builder()
            .id(10)
            .permissionEnum(PermissionEnum.PAYMENT_REFUND)
            .build();

    public static PermissionEntity SUPPORT_TICKET_READ = PermissionEntity.builder()
            .id(11)
            .permissionEnum(PermissionEnum.SUPPORT_TICKET_READ)
            .build();

    public static PermissionEntity SUPPORT_TICKET_RESPOND = PermissionEntity.builder()
            .id(12)
            .permissionEnum(PermissionEnum.SUPPORT_TICKET_RESPOND)
            .build();

    public static PermissionEntity CONTENT_CREATE = PermissionEntity.builder()
            .id(13)
            .permissionEnum(PermissionEnum.CONTENT_CREATE)
            .build();

    public static PermissionEntity CONTENT_UPDATE = PermissionEntity.builder()
            .id(14)
            .permissionEnum(PermissionEnum.CONTENT_UPDATE)
            .build();

    public static PermissionEntity CONTENT_DELETE = PermissionEntity.builder()
            .id(15)
            .permissionEnum(PermissionEnum.CONTENT_DELETE)
            .build();

    public static PermissionEntity SYSTEM_LOG_READ = PermissionEntity.builder()
            .id(16)
            .permissionEnum(PermissionEnum.SYSTEM_LOG_READ)
            .build();


    // RoleEntity instances
    public static RoleEntity ROLE_ADMIN = RoleEntity.builder()
            .id(1)
            .roleEnum(RoleEnum.ADMIN)
            .permissions(Set.of(
                    USER_READ, USER_CREATE, USER_UPDATE, USER_DELETE,
                    BOOKING_READ, BOOKING_CREATE, BOOKING_UPDATE, BOOKING_DELETE,
                    PAYMENT_READ, PAYMENT_REFUND, SUPPORT_TICKET_READ, SUPPORT_TICKET_RESPOND,
                    CONTENT_CREATE, CONTENT_UPDATE, CONTENT_DELETE, SYSTEM_LOG_READ
            ))
            .build();

    public static RoleEntity ROLE_OPERATOR = RoleEntity.builder()
            .id(2)
            .roleEnum(RoleEnum.OPERATOR)
            .permissions(Set.of(
                    USER_READ, USER_CREATE, USER_UPDATE,
                    BOOKING_READ, BOOKING_CREATE, BOOKING_UPDATE, BOOKING_DELETE,
                    PAYMENT_READ
            ))
            .build();

    public static RoleEntity ROLE_CUSTOMER = RoleEntity.builder()
            .id(3)
            .roleEnum(RoleEnum.CUSTOMER)
            .permissions(Set.of(
                    BOOKING_READ, BOOKING_CREATE, BOOKING_UPDATE,
                    USER_READ, USER_UPDATE, PAYMENT_READ
            ))
            .build();

    public static RoleEntity ROLE_GUEST = RoleEntity.builder()
            .id(4)
            .roleEnum(RoleEnum.GUEST)
            .permissions(Set.of())
            .build();

    public static RoleEntity ROLE_SUPPORT = RoleEntity.builder()
            .id(5)
            .roleEnum(RoleEnum.SUPPORT)
            .permissions(Set.of(
                    PAYMENT_READ, PAYMENT_REFUND,
                    SUPPORT_TICKET_READ, SUPPORT_TICKET_RESPOND
            ))
            .build();

    public static RoleEntity ROLE_CONTENT_MANAGER = RoleEntity.builder()
            .id(6)
            .roleEnum(RoleEnum.CONTENT_MANAGER)
            .permissions(Set.of(
                    CONTENT_CREATE, CONTENT_UPDATE, CONTENT_DELETE
            ))
            .build();


    public static UserEntity userAdmin() {
        // UserEntity instance
        return UserEntity.builder()
                .id(1L)
                .name("admin")
                .lastname("admin")
                .email("admin@example.com")
                .dni("1-111-1111")
                .role_id(ROLE_ADMIN)
                .passwordHash("adminpassword")
                .build();
    }

    public static UserEntity userOperator() {
        // UserEntity instance
        return UserEntity.builder()
                .id(2L)
                .name("operator")
                .lastname("operator")
                .email("operator@example.com")
                .dni("2-222-2222")
                .role_id(ROLE_OPERATOR)
                .passwordHash("operatorpassword")
                .build();
    }

    public static UserEntity userCustomer() {
        // UserEntity instance
        return UserEntity.builder()
                .id(3L)
                .name("customer")
                .lastname("customer")
                .email("customer@example.com")
                .dni("3-333-3333")
                .role_id(ROLE_CUSTOMER)
                .passwordHash("customerpassword")
                .build();
    }

    public static UserEntity userGuest() {
        // UserEntity instance
        return UserEntity.builder()
                .id(4L)
                .name("guest")
                .lastname("guest")
                .email("guest@example.com")
                .dni("4-444-4444")
                .role_id(ROLE_GUEST)
                .passwordHash("guestpassword")
                .build();
    }

    public static UserEntity userSupport() {
        // UserEntity instance
        return UserEntity.builder()
                .id(5L)
                .name("support")
                .lastname("support")
                .email("support@example.com")
                .dni("5-555-5555")
                .role_id(ROLE_SUPPORT)
                .passwordHash("supportpassword")
                .build();
    }

    public static UserEntity userContentManager() {
        // UserEntity instance
        return UserEntity.builder()
                .id(6L)
                .name("content")
                .lastname("manager")
                .email("contentmanager@example.com")
                .dni("6-666-6666")
                .role_id(ROLE_CONTENT_MANAGER)
                .passwordHash("contentmanagerpassword")
                .build();
    }
    
    public static List<UserEntity> userListMocks() {
        return List.of(
                userAdmin(),
                userOperator(),
                userCustomer(),
                userGuest(),
                userSupport(),
                userContentManager()
        );
    }

    /*
    * UserRequest instance
    * UserResponse instance
    * DTOs
     */
    private static UserResponse convertToResponseDTO(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getDni(),
                user.getName(),
                user.getLastname(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getCreatedBy(),
                user.getUpdatedBy(),
                user.getRole_id().getId()
        );
    }

    public static UserRequest userRequestMock = new UserRequest("1-111-1111", "admin", "admin", "admin@example.com", "adminpassword", null);
    public static UserResponse userResponseMock = convertToResponseDTO(userAdmin());
    public static List<UserResponse> userResponseListMocks() {
        return List.of(
                convertToResponseDTO(userAdmin()),
                convertToResponseDTO(userOperator()),
                convertToResponseDTO(userCustomer()),
                convertToResponseDTO(userGuest()),
                convertToResponseDTO(userSupport())
        );
    }



    /*
    * Auth instances
    * Here we have three instances of Auth
    *
     */
    public static AuthCreateUserRequest userAuthCreateUserRequestMock() {
        return new AuthCreateUserRequest("admin", "admin", "1-111-1111", "admin@example.com", "adminpassword");
    }

    public static AuthLoginRequest userAuthLoginRequestMock() {
        return new AuthLoginRequest("admin", "adminpassword");
    }

    public static AuthResponse userAuthResponseMock() {
        return new AuthResponse("admin", "Welcome admin", "jwt", true);
    }



    /**
     * Province instances
     * Here we have all provinces in Panama
     *
     */
    // Province instances
    public static Province provinceBocasMock = Province.builder()
            .id(1)
            .name("Bocas del Toro")
            .build();

    public static Province provinceCocleMock = Province.builder()
            .id(2)
            .name("Coclé")
            .build();

    public static Province provinceColonMock = Province.builder()
            .id(3)
            .name("Colón")
            .build();

    public static Province provinceChiriquiMock = Province.builder()
            .id(4)
            .name("Chiriqui")
            .build();

    public static Province provinceHerraraMock = Province.builder()
            .id(5)
            .name("Herrera")
            .build();

    public static List<Province> provinceListsMock = List.of(
            provinceBocasMock,
            provinceCocleMock,
            provinceColonMock,
            provinceChiriquiMock,
            provinceHerraraMock
    );

    private static ProvinceResponse convertToResponseDTO(Province province) {
        return new ProvinceResponse(province.getId(), province.getName());
    }

    public static ProvinceRequest provinceRequestMock = new ProvinceRequest("Bocas del Toro");
    public static ProvinceResponse provinceResponseMock = new ProvinceResponse(1, "Bocas del Toro");
    public static List<ProvinceResponse> provinceResponseListMocks = List.of(
            convertToResponseDTO(provinceBocasMock),
            convertToResponseDTO(provinceCocleMock),
            convertToResponseDTO(provinceColonMock),
            convertToResponseDTO(provinceChiriquiMock),
            convertToResponseDTO(provinceHerraraMock)
    );


    /**
     * District instances
     * Here we have five districts
     *
     */
    public static District districtAlmiranteMock = District.builder()
            .id(1)
            .name("Almirante")
            .province(provinceBocasMock)
            .build();

    public static District districtBocasMock = District.builder()
            .id(2)
            .name("Bocas del Toro")
            .province(provinceBocasMock)
            .build();

    public static District districtChanguinolaMock = District.builder()
            .id(3)
            .name("Changuinola")
            .province(provinceBocasMock)
            .build();

    public static District districtAntonMock = District.builder()
            .id(4)
            .name("Antón")
            .province(provinceCocleMock)
            .build();

    public static District districtPenonomeMock = District.builder()
            .id(5)
            .name("Penonomé")
            .province(provinceCocleMock)
            .build();

    public static List<District> districtListsMock = List.of(
            districtAlmiranteMock,
            districtBocasMock,
            districtChanguinolaMock,
            districtAntonMock,
            districtPenonomeMock
    );

    public static List<District> districtListBocasMock = List.of(
            districtAlmiranteMock,
            districtBocasMock,
            districtChanguinolaMock
    );

    private static DistrictResponse convertToResponseDTO(District district) {
        return new DistrictResponse(district.getId(), district.getName(), district.getProvince().getId());
    }

    public static DistrictRequest districtRequestMock = new DistrictRequest("Almirante", 1);
    public static DistrictResponse districtResponseMock = convertToResponseDTO(districtAlmiranteMock);
    public static List<DistrictResponse> districtResponseListMocks = List.of(
            convertToResponseDTO(districtAlmiranteMock),
            convertToResponseDTO(districtBocasMock),
            convertToResponseDTO(districtChanguinolaMock),
            convertToResponseDTO(districtAntonMock),
            convertToResponseDTO(districtPenonomeMock)
    );



    /*
    * Address instances
    *
     */
    public static Address addressOneMock = Address.builder()
            .addressId(1)
            .street("Street One")
            .postalCode("1111")
            .district(districtAlmiranteMock)
            .additionalInfo("Additional info")
            .build();

    public static Address addressTwoMock = Address.builder()
            .addressId(2)
            .street("Street Two")
            .postalCode("2222")
            .district(districtBocasMock)
            .additionalInfo("Additional info")
            .build();

    public static Address addressThreeMock = Address.builder()
            .addressId(3)
            .street("Street Three")
            .postalCode("3333")
            .district(districtChanguinolaMock)
            .additionalInfo("Additional info")
            .build();

    public static List<Address> addressListsMock = List.of(
            addressOneMock,
            addressTwoMock,
            addressThreeMock
    );

    public static AddressResponse addressResponseMock = new AddressResponse(addressOneMock);
    public static AddressRequest addressRequestMock = new AddressRequest("Street One", "1111", 1, "Additional info");

    public static List<AddressResponse> addressResponsesListMocks = List.of(
            new AddressResponse(addressOneMock),
            new AddressResponse(addressTwoMock),
            new AddressResponse(addressThreeMock)
    );


    /*
     * Provider instances
     *
     */

    public static Provider providerOneMock = Provider.builder()
            .id(1)
            .ruc("1-111-1111")
            .name("Provider One")
            .email("provideone@example.com")
            .phone("+50761011111")
            .province(provinceCocleMock)
            .district(districtAlmiranteMock)
            .address(addressOneMock)
            .build();

    public static Provider providerTwoMock = Provider.builder()
            .id(2)
            .ruc("2-222-2222")
            .name("Provider Two")
            .email("providertwo@example.com")
            .phone("+50761022222")
            .province(provinceColonMock)
            .district(districtBocasMock)
            .address(addressTwoMock)
            .build();

    public static Provider providerThreeMock = Provider.builder()
            .id(3)
            .ruc("3-333-3333")
            .name("Provider Three")
            .email("providerthree@example.com")
            .phone("+50761033333")
            .province(provinceCocleMock)
            .district(districtChanguinolaMock)
            .address(addressThreeMock)
            .build();

    public static List<Provider> providerListsMock = List.of(
            providerOneMock,
            providerTwoMock,
            providerThreeMock
    );

    public static ProviderResponse providerResponseMock = new ProviderResponse(providerOneMock);
    public static ProviderRequest providerRequestMock = new ProviderRequest(
            "1-111-1111",
            "Provider One",
            "providertwo@example.com",
            "+50761011111",
            2,
            1,
            1);

    public static List<ProviderResponse> providerResponseListMocks = List.of(
            new ProviderResponse(providerOneMock),
            new ProviderResponse(providerTwoMock),
            new ProviderResponse(providerThreeMock)
    );
}
