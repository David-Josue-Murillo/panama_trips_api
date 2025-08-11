package com.app.panama_trips;

import com.app.panama_trips.persistence.entity.*;
import com.app.panama_trips.presentation.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
            .createdAt(LocalDateTime.now())
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
            .createdAt(LocalDateTime.now())
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
            .createdAt(LocalDateTime.now())
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



    /*
    * TourPlan instances
    *
    *
     */

    public static TourPlan tourPlanOneMock = TourPlan.builder()
            .id(1)
            .title("Tour Plan One")
            .description("Description One")
            .price(BigDecimal.valueOf(100.00))
            .duration(1)
            .availableSpots(10)
            .provider(providerOneMock)
            .createdAt(LocalDateTime.now())
            .build();

    public static TourPlan tourPlanTwoMock = TourPlan.builder()
            .id(2)
            .title("Tour Plan Two")
            .description("Description Two")
            .price(BigDecimal.valueOf(200.00))
            .duration(2)
            .availableSpots(20)
            .provider(providerTwoMock)
            .createdAt(LocalDateTime.now())
            .build();

    public static TourPlan tourPlanThreeMock = TourPlan.builder()
            .id(3)
            .title("Tour Plan Three")
            .description("Description Three")
            .price(BigDecimal.valueOf(300.00))
            .duration(3)
            .availableSpots(30)
            .provider(providerThreeMock)
            .createdAt(LocalDateTime.now())
            .build();

    public static List<TourPlan> tourPlanListsMock = List.of(
            tourPlanOneMock,
            tourPlanTwoMock,
            tourPlanThreeMock
    );

    public static TourPlanResponse tourPlanResponseMock = new TourPlanResponse(tourPlanOneMock);
    public static TourPlanRequest tourPlanRequestMock = new TourPlanRequest(
            "Tour Plan One",
            "Description One",
            BigDecimal.valueOf(100.00),
            1,
            10,
            1);

    public static List<TourPlanResponse> tourPlanResponseListMocks = List.of(
            new TourPlanResponse(tourPlanOneMock),
            new TourPlanResponse(tourPlanTwoMock),
            new TourPlanResponse(tourPlanThreeMock)
    );

    /*
    * Comarca instances
    *
    *
     */

    public static Comarca comarcaOneMock = Comarca.builder()
            .id(1)
            .name("Comarca One")
            .build();

    public static Comarca comarcaTwoMock = Comarca.builder()
            .id(2)
            .name("Comarca Two")
            .build();

    public static Comarca comarcaThreeMock = Comarca.builder()
            .id(3)
            .name("Comarca Three")
            .build();

    public static List<Comarca> comarcaListsMock = List.of(
            comarcaOneMock,
            comarcaTwoMock,
            comarcaThreeMock
    );

    /*
     * Region Instance
     *
     *
     */

    public static Region regionOneMock = Region.builder()
            .id(1)
            .name("Region One")
            .province(provinceBocasMock)
            .comarca(comarcaOneMock)
            .build();

    public static Region regionTwoMock = Region.builder()
            .id(2)
            .name("Region Two")
            .province(provinceCocleMock)
            .comarca(comarcaTwoMock)
            .build();

    public static Region regionThreeMock = Region.builder()
            .id(3)
            .name("Region Three")
            .province(provinceColonMock)
            .comarca(comarcaThreeMock)
            .build();

    public static List<Region> regionListsMock = List.of(
            regionOneMock,
            regionTwoMock,
            regionThreeMock
    );

    public static RegionResponse regionResponseMock = new RegionResponse(regionOneMock);
    public static RegionRequest regionRequestMock = new RegionRequest("Region One", 1, 1);
    public static List<RegionResponse> regionResponseListMocks = List.of(
            new RegionResponse(regionOneMock),
            new RegionResponse(regionTwoMock),
            new RegionResponse(regionThreeMock)
    );


    /*
     * Reservation instances
     *
     *
     */
    public static Reservation reservationOneMock = Reservation.builder()
            .id(1)
            .user(userCustomer())
            .tourPlan(tourPlanOneMock)
            .reservationDate(LocalDate.now())
            .reservationStatus(ReservationStatus.pending)
            .totalPrice(BigDecimal.valueOf(100.00))
            .build();

    public static Reservation reservationTwoMock = Reservation.builder()
            .id(2)
            .user(userCustomer())
            .tourPlan(tourPlanTwoMock)
            .reservationDate(LocalDate.now())
            .reservationStatus(ReservationStatus.pending)
            .totalPrice(BigDecimal.valueOf(200.00))
            .build();

    public static Reservation reservationThreeMock = Reservation.builder()
            .id(3)
            .user(userCustomer())
            .tourPlan(tourPlanThreeMock)
            .reservationDate(LocalDate.now())
            .reservationStatus(ReservationStatus.pending)
            .totalPrice(BigDecimal.valueOf(300.00))
            .build();

    public static List<Reservation> reservationListsMock = List.of(
            reservationOneMock,
            reservationTwoMock,
            reservationThreeMock
    );

    public static ReservationResponse reservationResponseMock = new ReservationResponse(reservationOneMock);
    public static ReservationRequest reservationRequestMock = new ReservationRequest(
            1L,
            1,
            LocalDate.now(),
            BigDecimal.valueOf(100.00)
    );

    public static List<ReservationResponse> reservationResponseListMocks = List.of(
            new ReservationResponse(reservationOneMock),
            new ReservationResponse(reservationTwoMock),
            new ReservationResponse(reservationThreeMock)
    );

    /*
     * TourPlanImage instances
     *
     *
     */

    public static TourPlanImage tourPlanImageOneMock = TourPlanImage.builder()
            .id(1)
            .tourPlan(tourPlanOneMock)
            .imageUrl("https://example.com/image1.jpg")
            .altText("Image 1")
            .isMain(true)
            .displayOrder(1)
            .build();

    public static TourPlanImage tourPlanImageTwoMock = TourPlanImage.builder()
            .id(2)
            .tourPlan(tourPlanOneMock)
            .imageUrl("https://example.com/image2.jpg")
            .altText("Image 2")
            .isMain(false)
            .displayOrder(2)
            .build();

    public static TourPlanImage tourPlanImageThreeMock = TourPlanImage.builder()
            .id(3)
            .tourPlan(tourPlanTwoMock)
            .imageUrl("https://example.com/image3.jpg")
            .altText("Image 3")
            .isMain(false)
            .displayOrder(3)
            .build();

    public static List<TourPlanImage> tourPlanImageListsMock = List.of(
            tourPlanImageOneMock,
            tourPlanImageTwoMock,
            tourPlanImageThreeMock
    );

    public static TourPlanImageResponse tourPlanImageResponseMock = new TourPlanImageResponse(tourPlanImageOneMock);
    public static TourPlanImageRequest tourPlanImageRequestMock = new TourPlanImageRequest(
            1,
            "https://example.com/image1.jpg",
            "Image 1",
            true,
            1
    );

    public static List<TourPlanImageResponse> tourPlanImageResponseListMocks = List.of(
            new TourPlanImageResponse(tourPlanImageOneMock),
            new TourPlanImageResponse(tourPlanImageTwoMock),
            new TourPlanImageResponse(tourPlanImageThreeMock)
    );

    /*
    * TourPlanAvailability instances
    *
     */

    public static TourPlanAvailability tourPlanAvailabilityOneMock = TourPlanAvailability.builder()
            .id(1)
            .tourPlan(tourPlanOneMock)
            .availableDate(LocalDate.now())
            .availableSpots(10)
            .isAvailable(true)
            .priceOverride(BigDecimal.valueOf(90.00))
            .build();

    public static TourPlanAvailability tourPlanAvailabilityTwoMock = TourPlanAvailability.builder()
            .id(2)
            .tourPlan(tourPlanOneMock)
            .availableDate(LocalDate.now().plusDays(1))
            .availableSpots(5)
            .isAvailable(true)
            .priceOverride(BigDecimal.valueOf(80.00))
            .build();

    public static TourPlanAvailability tourPlanAvailabilityThreeMock = TourPlanAvailability.builder()
            .id(3)
            .tourPlan(tourPlanTwoMock)
            .availableDate(LocalDate.now().plusDays(2))
            .availableSpots(15)
            .isAvailable(true)
            .priceOverride(BigDecimal.valueOf(70.00))
            .build();

    public static List<TourPlanAvailability> tourPlanAvailabilityListsMock = List.of(
            tourPlanAvailabilityOneMock,
            tourPlanAvailabilityTwoMock,
            tourPlanAvailabilityThreeMock
    );

    public static TourPlanAvailabilityResponse tourPlanAvailabilityResponseMock = new TourPlanAvailabilityResponse(tourPlanAvailabilityOneMock);
    public static TourPlanAvailabilityRequest tourPlanAvailabilityRequest = new TourPlanAvailabilityRequest(
            1,
            1,
            LocalDate.now(),
            10,
            true,
            BigDecimal.valueOf(90.00)
    );

    public static List<TourPlanAvailabilityResponse> tourPlanAvailabilityResponseListMocks = List.of(
            new TourPlanAvailabilityResponse(tourPlanAvailabilityOneMock),
            new TourPlanAvailabilityResponse(tourPlanAvailabilityTwoMock),
            new TourPlanAvailabilityResponse(tourPlanAvailabilityThreeMock)
    );


    /*
     * TourPlanSpecialPrice instances
     *
     *
     */

    public static TourPlanSpecialPrice tourPlanSpecialPriceOneMock = TourPlanSpecialPrice.builder()
            .id(1)
            .tourPlan(tourPlanOneMock)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(5))
            .price(BigDecimal.valueOf(80.00))
            .description("Description One")
            .build();

    public static TourPlanSpecialPrice tourPlanSpecialPriceTwoMock = TourPlanSpecialPrice.builder()
            .id(2)
            .tourPlan(tourPlanTwoMock)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(10))
            .price(BigDecimal.valueOf(80.00))
            .description("Description Two")
            .build();

    public static TourPlanSpecialPrice tourPlanSpecialPriceThreeMock = TourPlanSpecialPrice.builder()
            .id(3)
            .tourPlan(tourPlanThreeMock)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(15))
            .price(BigDecimal.valueOf(80.00))
            .description("Description Three")
            .build();

    public static List<TourPlanSpecialPrice> tourPlanSpecialPriceListsMock = List.of(
            tourPlanSpecialPriceOneMock,
            tourPlanSpecialPriceTwoMock,
            tourPlanSpecialPriceThreeMock
    );

    public static TourPlanSpecialPriceResponse tourPlanSpecialPriceResponseMock = new TourPlanSpecialPriceResponse(tourPlanSpecialPriceOneMock);
    public static TourPlanSpecialPriceRequest tourPlanSpecialPriceRequestMock = new TourPlanSpecialPriceRequest(
            1,
            LocalDate.now(),
            LocalDate.now().plusDays(5),
            BigDecimal.valueOf(80.00),
            "Description One"
    );

    public static List<TourPlanSpecialPriceResponse> tourPlanSpecialPriceResponseListMocks = List.of(
            new TourPlanSpecialPriceResponse(tourPlanSpecialPriceOneMock),
            new TourPlanSpecialPriceResponse(tourPlanSpecialPriceTwoMock),
            new TourPlanSpecialPriceResponse(tourPlanSpecialPriceThreeMock)
    );


    /*
     * CancellationPolicy Instance
     *
     *
     */

    public static CancellationPolicy cancellationPolicyOneMock = CancellationPolicy.builder()
            .id(1)
            .name("Test one")
            .description("Description one")
            .refundPercentage(20)
            .daysBeforeTour(5)
            .build();

    public static CancellationPolicy cancellationPolicyTwoMock = CancellationPolicy.builder()
            .id(2)
            .name("Test two")
            .description("Description two")
            .refundPercentage(25)
            .daysBeforeTour(10)
            .build();

    public static CancellationPolicy cancellationPolicyThreeMock = CancellationPolicy.builder()
            .id(3)
            .name("Test three")
            .description("Description three")
            .refundPercentage(25)
            .daysBeforeTour(15)
            .build();

    public static List<CancellationPolicy> cancellationPolicyListMock = List.of(
            cancellationPolicyOneMock,
            cancellationPolicyTwoMock,
            cancellationPolicyThreeMock
    );

    public static CancellationPolicyResponse cancellationPolicyResponseMock = new CancellationPolicyResponse(cancellationPolicyOneMock);
    public static CancellationPolicyRequest cancellationPolicyRequestMock = new CancellationPolicyRequest(
            "Test one",
            "Description one",
            20,
            5
    );

    public static List<CancellationPolicyResponse> cancellationPolicyResponseListMock = List.of(
            new CancellationPolicyResponse(cancellationPolicyOneMock),
            new CancellationPolicyResponse(cancellationPolicyTwoMock),
            new CancellationPolicyResponse(cancellationPolicyThreeMock)
    );

    /*
    * Guide instances
    *
    *
    */

   public static Guide guideOneMock = Guide.builder()
        .id(1)
        .user(userAdmin())
        .provider(providerOneMock)
        .bio("Bio one")
        .specialties("Specialty one, Specialty two, Specialty three")
        .languages("Language one, Language two, Language three")
        .yearsExperience(5)
        .certificationDetails("Certification one, Certification two, Certification three")
        .isActive(true)
        .build();

    public static Guide guideTwoMock = Guide.builder()
            .id(2)
            .user(userOperator())
            .provider(providerTwoMock)
            .bio("Bio two")
            .specialties("Specialty one, Specialty two, Specialty three")
            .languages("Language one, Language two, Language three")
            .yearsExperience(5)
            .certificationDetails("Certification one, Certification two, Certification three")
            .isActive(true)
            .build();

    public static Guide guideThreeMock = Guide.builder()
            .id(3)
            .user(userCustomer())
            .provider(providerThreeMock)
            .bio("Bio three")
            .specialties("Specialty one, Specialty two, Specialty three")
            .languages("Language one, Language two, Language three")
            .yearsExperience(5)
            .certificationDetails("Certification one, Certification two, Certification three")
            .isActive(true)
            .build();

    public static List<Guide> guideListMock = List.of(
            guideOneMock,
            guideTwoMock,
            guideThreeMock
    );

    public static GuideResponse guideResponseMock = new GuideResponse(guideOneMock);
    public static GuideRequest guideRequestMock = new GuideRequest(
            1L,
            1,
            "Bio one",
            List.of("Specialty one", "Specialty two", "Specialty three"),
            List.of("Language one", "Language two", "Language three"),
            5,
            "Certification one, Certification two, Certification three",
            true
    );

    public static List<GuideResponse> guideResponseListMock = List.of(
            new GuideResponse(guideOneMock),
            new GuideResponse(guideTwoMock),
            new GuideResponse(guideThreeMock)
    );

    /*
     * TourAssignment instances
     */
    public static TourAssignment tourAssignmentOneMock = TourAssignment.builder()
            .id(1)
            .guide(guideOneMock)
            .tourPlan(tourPlanOneMock)
            .reservationDate(LocalDate.now().plusDays(7))
            .status("ASSIGNED")
            .notes("Test notes for assignment one")
            .createdAt(LocalDate.now())
            .build();

    public static TourAssignment tourAssignmentTwoMock = TourAssignment.builder()
            .id(2)
            .guide(guideTwoMock)
            .tourPlan(tourPlanTwoMock)
            .reservationDate(LocalDate.now().plusDays(14))
            .status("COMPLETED")
            .notes("Test notes for assignment two")
            .createdAt(LocalDate.now())
            .build();

    public static TourAssignment tourAssignmentThreeMock = TourAssignment.builder()
            .id(3)
            .guide(guideThreeMock)
            .tourPlan(tourPlanThreeMock)
            .reservationDate(LocalDate.now().plusDays(21))
            .status("CANCELLED")
            .notes("Test notes for assignment three")
            .createdAt(LocalDate.now())
            .build();

    public static List<TourAssignment> tourAssignmentListMock = List.of(
            tourAssignmentOneMock,
            tourAssignmentTwoMock,
            tourAssignmentThreeMock
    );

    public static TourAssignmentRequest tourAssignmentRequestMock = new TourAssignmentRequest(
            1,  // guideId
            1,  // tourPlanId
            LocalDate.now().plusDays(7),  // reservationDate
            "ASSIGNED",  // status
            "Test notes for new assignment"  // notes
    );

    public static TourAssignmentResponse tourAssignmentResponseMock = new TourAssignmentResponse(tourAssignmentOneMock);

    public static List<TourAssignmentResponse> tourAssignmentResponseListMock = List.of(
        tourAssignmentResponseMock,
        new TourAssignmentResponse(tourAssignmentTwoMock),
        new TourAssignmentResponse(tourAssignmentThreeMock)
    );


    /*
     * NotificationTemplate instances
     * 
     * 
     */

     public static NotificationTemplate notificationTemplateOneMock() {
        return NotificationTemplate.builder()
                .id(1)
                .name("Test one")
                .subject("Subject one")
                .body("Hello ${name}, your email is ${email}")
                .type("EMAIL")
                .variables("name,email")
                .build();
     }

     public static NotificationTemplate notificationTemplateTwoMock() {
        return NotificationTemplate.builder()
                .id(2)
                .name("Test two")
                .subject("Subject two")
                .body("Hello ${name}, your email is ${email}")
                .type("EMAIL")
                .variables("name,email")
                .build();
     }

     public static NotificationTemplate notificationTemplateThreeMock() {
        return NotificationTemplate.builder()
                .id(3)
                .name("Test three")
                .subject("Subject three")
                .body("Hello ${name}, your email is ${email}")
                .type("EMAIL")
                .variables("name,email")
                .build();
     }

     public static List<NotificationTemplate> notificationTemplateListMock() {
        return List.of(
                notificationTemplateOneMock(),
                notificationTemplateTwoMock(),
                notificationTemplateThreeMock()
        );
     }

     public static NotificationTemplateResponse notificationTemplateResponseMock = new NotificationTemplateResponse(notificationTemplateOneMock());
     public static NotificationTemplateRequest notificationTemplateRequestMock = new NotificationTemplateRequest(
            "Test one",
            "Subject one",
            "Hello ${name}, your email is ${email}",
            "EMAIL",
            "name,email"
     );

     public static List<NotificationTemplateResponse> notificationTemplateResponseListMock = List.of(
            new NotificationTemplateResponse(notificationTemplateOneMock()),
            new NotificationTemplateResponse(notificationTemplateTwoMock()),
            new NotificationTemplateResponse(notificationTemplateThreeMock())
     );

     /*
     * NotificationHistory instances
     * 
     * 
     */
    public static NotificationHistory notificationHistoryOneMock() {
        return NotificationHistory.builder()
            .id(1)
            .template(notificationTemplateOneMock())
            .user(userAdmin())
            .reservation(reservationOneMock)
            .sentAt(LocalDateTime.now())
            .deliveryStatus("PENDING")
            .content("Test Content")
            .channel("Channel one")
            .build();}

    public static NotificationHistory notificationHistoryTwoMock() {
        return NotificationHistory.builder()
            .id(2)
            .template(notificationTemplateTwoMock()) 
            .user(userContentManager())
            .reservation(reservationTwoMock) 
            .sentAt(LocalDateTime.now().minusDays(1))
            .deliveryStatus("DELIVERED")
            .content("Contenido de prueba 2")
            .channel("Channel two")
            .build();
    }
    
    public static NotificationHistory notificationHistoryThreeMock() {
        return NotificationHistory.builder()
            .id(3)
            .template(notificationTemplateThreeMock()) 
            .user(userCustomer())
            .reservation(reservationThreeMock) 
            .sentAt(LocalDateTime.now().minusHours(5))
            .deliveryStatus("FAILED")
            .content("Contenido de prueba 3")
            .channel("Channel three")
            .build();
            }

    public static List<NotificationHistory> notificationHistoriesListMock() {
        return List.of(
            notificationHistoryTwoMock(),
            notificationHistoryTwoMock(),
            notificationHistoryThreeMock()
        );
    }

    public static NotificationHistoryResponse notificationHistoryResponse = new NotificationHistoryResponse(notificationHistoryOneMock());
    public static NotificationHistoryRequest notificationHistoryRequest = new NotificationHistoryRequest(
        1,
        1L,
        1,
        "PENDING",
        "Test Content",
        "Channel one"
    );

    public static List<NotificationHistoryResponse> notificationHistoryListResponse = List.of(
        notificationHistoryResponse,
        new NotificationHistoryResponse(notificationHistoryTwoMock()),
        new NotificationHistoryResponse(notificationHistoryThreeMock())
    );

  /*
   * PaymentInstallment instances
   * 
   * 
   */
  public static PaymentInstallment paymentInstallmentOneMock() {
    return PaymentInstallment.builder()
        .id(1)
        .reservation(reservationOneMock)
        .amount(BigDecimal.valueOf(100.00))
        .dueDate(LocalDate.now().plusDays(30))
        .payment(null)
        .status("PENDING")
        .reminderSent(false)
        .createdAt(LocalDate.now())
        .build();
  }

  public static PaymentInstallment paymentInstallmentTwoMock() {
    return PaymentInstallment.builder()
        .id(2)
        .reservation(reservationTwoMock)
        .amount(BigDecimal.valueOf(200.00))
        .dueDate(LocalDate.now().minusDays(5))
        .payment(null)
        .status("OVERDUE")
        .reminderSent(true)
        .createdAt(LocalDate.now().minusDays(10))
        .build();
  }

  public static PaymentInstallment paymentInstallmentThreeMock() {
    return PaymentInstallment.builder()
        .id(3)
        .reservation(reservationThreeMock)
        .amount(BigDecimal.valueOf(150.00))
        .dueDate(LocalDate.now().plusDays(15))
        .payment(null)
        .status("PAID")
        .reminderSent(false)
        .createdAt(LocalDate.now().minusDays(5))
        .build();
  }

  public static List<PaymentInstallment> paymentInstallmentListMock() {
    return List.of(
        paymentInstallmentOneMock(),
        paymentInstallmentTwoMock(),
        paymentInstallmentThreeMock()
        );
  }

  public static PaymentInstallmentResponse paymentInstallmentResponseMock = new PaymentInstallmentResponse(
      paymentInstallmentOneMock());

  public static PaymentInstallmentRequest paymentInstallmentRequestMock = new PaymentInstallmentRequest(
      1,
      BigDecimal.valueOf(100.00),
      LocalDate.now().plusDays(30),
      null,
      "PENDING",
      false);

  public static List<PaymentInstallmentResponse> paymentInstallmentResponseListMock = List.of(
      new PaymentInstallmentResponse(paymentInstallmentOneMock()),
      new PaymentInstallmentResponse(paymentInstallmentTwoMock()),
      new PaymentInstallmentResponse(paymentInstallmentThreeMock())
    );

    // Additional mock data for specific test scenarios
  public static PaymentInstallment pendingInstallmentMock() {
    return PaymentInstallment.builder()
        .id(7)
        .reservation(reservationOneMock)
        .amount(BigDecimal.valueOf(50.00))
        .dueDate(LocalDate.now().plusDays(5))
        .payment(null)
        .status("PENDING")
        .reminderSent(false)
        .createdAt(LocalDate.now())
        .build();
  }

  public static PaymentInstallment overdueInstallmentMock() {
    return PaymentInstallment.builder()
        .id(8)
        .reservation(reservationTwoMock)
        .amount(BigDecimal.valueOf(175.00))
        .dueDate(LocalDate.now().minusDays(10))
        .payment(null)
        .status("OVERDUE")
        .reminderSent(true)
        .createdAt(LocalDate.now().minusDays(15))
        .build();
  }

  public static PaymentInstallment paidInstallmentMock() {
    return PaymentInstallment.builder()
        .id(9)
        .reservation(reservationThreeMock)
        .amount(BigDecimal.valueOf(250.00))
        .dueDate(LocalDate.now().minusDays(1))
        .payment(null)
        .status("PAID")
        .reminderSent(false)
        .createdAt(LocalDate.now().minusDays(10))
        .build();
  }

  public static PaymentInstallment cancelledInstallmentMock() {
    return PaymentInstallment.builder()
        .id(10)
        .reservation(reservationOneMock)
        .amount(BigDecimal.valueOf(90.00))
        .dueDate(LocalDate.now().plusDays(20))
        .payment(null)
        .status("CANCELLED")
        .reminderSent(false)
        .createdAt(LocalDate.now().minusDays(5))
        .build();
  }

  
}
