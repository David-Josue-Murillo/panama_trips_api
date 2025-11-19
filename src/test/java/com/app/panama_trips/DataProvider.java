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
                    CONTENT_CREATE, CONTENT_UPDATE, CONTENT_DELETE, SYSTEM_LOG_READ))
            .build();

    public static RoleEntity ROLE_OPERATOR = RoleEntity.builder()
            .id(2)
            .roleEnum(RoleEnum.OPERATOR)
            .permissions(Set.of(
                    USER_READ, USER_CREATE, USER_UPDATE,
                    BOOKING_READ, BOOKING_CREATE, BOOKING_UPDATE, BOOKING_DELETE,
                    PAYMENT_READ))
            .build();

    public static RoleEntity ROLE_CUSTOMER = RoleEntity.builder()
            .id(3)
            .roleEnum(RoleEnum.CUSTOMER)
            .permissions(Set.of(
                    BOOKING_READ, BOOKING_CREATE, BOOKING_UPDATE,
                    USER_READ, USER_UPDATE, PAYMENT_READ))
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
                    SUPPORT_TICKET_READ, SUPPORT_TICKET_RESPOND))
            .build();

    public static RoleEntity ROLE_CONTENT_MANAGER = RoleEntity.builder()
            .id(6)
            .roleEnum(RoleEnum.CONTENT_MANAGER)
            .permissions(Set.of(
                    CONTENT_CREATE, CONTENT_UPDATE, CONTENT_DELETE))
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
                userContentManager());
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
                user.getRole_id().getId());
    }

    public static UserRequest userRequestMock = new UserRequest("1-111-1111", "admin", "admin", "admin@example.com",
            "adminpassword", null);
    public static UserResponse userResponseMock = convertToResponseDTO(userAdmin());

    public static List<UserResponse> userResponseListMocks() {
        return List.of(
                convertToResponseDTO(userAdmin()),
                convertToResponseDTO(userOperator()),
                convertToResponseDTO(userCustomer()),
                convertToResponseDTO(userGuest()),
                convertToResponseDTO(userSupport()));
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
            provinceHerraraMock);

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
            convertToResponseDTO(provinceHerraraMock));

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
            districtPenonomeMock);

    public static List<District> districtListBocasMock = List.of(
            districtAlmiranteMock,
            districtBocasMock,
            districtChanguinolaMock);

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
            convertToResponseDTO(districtPenonomeMock));

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
            addressThreeMock);

    public static AddressResponse addressResponseMock = new AddressResponse(addressOneMock);
    public static AddressRequest addressRequestMock = new AddressRequest("Street One", "1111", 1, "Additional info");

    public static List<AddressResponse> addressResponsesListMocks = List.of(
            new AddressResponse(addressOneMock),
            new AddressResponse(addressTwoMock),
            new AddressResponse(addressThreeMock));

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
            providerThreeMock);

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
            new ProviderResponse(providerThreeMock));

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
            tourPlanThreeMock);

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
            new TourPlanResponse(tourPlanThreeMock));

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
            comarcaThreeMock);

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
            regionThreeMock);

    public static RegionResponse regionResponseMock = new RegionResponse(regionOneMock);
    public static RegionRequest regionRequestMock = new RegionRequest("Region One", 1, 1);
    public static List<RegionResponse> regionResponseListMocks = List.of(
            new RegionResponse(regionOneMock),
            new RegionResponse(regionTwoMock),
            new RegionResponse(regionThreeMock));

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
            reservationThreeMock);

    public static ReservationResponse reservationResponseMock = new ReservationResponse(reservationOneMock);
    public static ReservationRequest reservationRequestMock = new ReservationRequest(
            1L,
            1,
            LocalDate.now(),
            BigDecimal.valueOf(100.00));

    public static List<ReservationResponse> reservationResponseListMocks = List.of(
            new ReservationResponse(reservationOneMock),
            new ReservationResponse(reservationTwoMock),
            new ReservationResponse(reservationThreeMock));

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
            tourPlanImageThreeMock);

    public static TourPlanImageResponse tourPlanImageResponseMock = new TourPlanImageResponse(tourPlanImageOneMock);
    public static TourPlanImageRequest tourPlanImageRequestMock = new TourPlanImageRequest(
            1,
            "https://example.com/image1.jpg",
            "Image 1",
            true,
            1);

    public static List<TourPlanImageResponse> tourPlanImageResponseListMocks = List.of(
            new TourPlanImageResponse(tourPlanImageOneMock),
            new TourPlanImageResponse(tourPlanImageTwoMock),
            new TourPlanImageResponse(tourPlanImageThreeMock));

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
            tourPlanAvailabilityThreeMock);

    public static TourPlanAvailabilityResponse tourPlanAvailabilityResponseMock = new TourPlanAvailabilityResponse(
            tourPlanAvailabilityOneMock);
    public static TourPlanAvailabilityRequest tourPlanAvailabilityRequest = new TourPlanAvailabilityRequest(
            1,
            1,
            LocalDate.now(),
            10,
            true,
            BigDecimal.valueOf(90.00));

    public static List<TourPlanAvailabilityResponse> tourPlanAvailabilityResponseListMocks = List.of(
            new TourPlanAvailabilityResponse(tourPlanAvailabilityOneMock),
            new TourPlanAvailabilityResponse(tourPlanAvailabilityTwoMock),
            new TourPlanAvailabilityResponse(tourPlanAvailabilityThreeMock));

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
            tourPlanSpecialPriceThreeMock);

    public static TourPlanSpecialPriceResponse tourPlanSpecialPriceResponseMock = new TourPlanSpecialPriceResponse(
            tourPlanSpecialPriceOneMock);
    public static TourPlanSpecialPriceRequest tourPlanSpecialPriceRequestMock = new TourPlanSpecialPriceRequest(
            1,
            LocalDate.now(),
            LocalDate.now().plusDays(5),
            BigDecimal.valueOf(80.00),
            "Description One");

    public static List<TourPlanSpecialPriceResponse> tourPlanSpecialPriceResponseListMocks = List.of(
            new TourPlanSpecialPriceResponse(tourPlanSpecialPriceOneMock),
            new TourPlanSpecialPriceResponse(tourPlanSpecialPriceTwoMock),
            new TourPlanSpecialPriceResponse(tourPlanSpecialPriceThreeMock));

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
            cancellationPolicyThreeMock);

    public static CancellationPolicyResponse cancellationPolicyResponseMock = new CancellationPolicyResponse(
            cancellationPolicyOneMock);
    public static CancellationPolicyRequest cancellationPolicyRequestMock = new CancellationPolicyRequest(
            "Test one",
            "Description one",
            20,
            5);

    public static List<CancellationPolicyResponse> cancellationPolicyResponseListMock = List.of(
            new CancellationPolicyResponse(cancellationPolicyOneMock),
            new CancellationPolicyResponse(cancellationPolicyTwoMock),
            new CancellationPolicyResponse(cancellationPolicyThreeMock));

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
            guideThreeMock);

    public static GuideResponse guideResponseMock = new GuideResponse(guideOneMock);
    public static GuideRequest guideRequestMock = new GuideRequest(
            1L,
            1,
            "Bio one",
            List.of("Specialty one", "Specialty two", "Specialty three"),
            List.of("Language one", "Language two", "Language three"),
            5,
            "Certification one, Certification two, Certification three",
            true);

    public static List<GuideResponse> guideResponseListMock = List.of(
            new GuideResponse(guideOneMock),
            new GuideResponse(guideTwoMock),
            new GuideResponse(guideThreeMock));

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
            tourAssignmentThreeMock);

    public static TourAssignmentRequest tourAssignmentRequestMock = new TourAssignmentRequest(
            1, // guideId
            1, // tourPlanId
            LocalDate.now().plusDays(7), // reservationDate
            "ASSIGNED", // status
            "Test notes for new assignment" // notes
    );

    public static TourAssignmentResponse tourAssignmentResponseMock = new TourAssignmentResponse(tourAssignmentOneMock);

    public static List<TourAssignmentResponse> tourAssignmentResponseListMock = List.of(
            tourAssignmentResponseMock,
            new TourAssignmentResponse(tourAssignmentTwoMock),
            new TourAssignmentResponse(tourAssignmentThreeMock));

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
                notificationTemplateThreeMock());
    }

    public static NotificationTemplateResponse notificationTemplateResponseMock = new NotificationTemplateResponse(
            notificationTemplateOneMock());
    public static NotificationTemplateRequest notificationTemplateRequestMock = new NotificationTemplateRequest(
            "Test one",
            "Subject one",
            "Hello ${name}, your email is ${email}",
            "EMAIL",
            "name,email");

    public static List<NotificationTemplateResponse> notificationTemplateResponseListMock = List.of(
            new NotificationTemplateResponse(notificationTemplateOneMock()),
            new NotificationTemplateResponse(notificationTemplateTwoMock()),
            new NotificationTemplateResponse(notificationTemplateThreeMock()));

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
                .build();
    }

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
                notificationHistoryThreeMock());
    }

    public static NotificationHistoryResponse notificationHistoryResponse = new NotificationHistoryResponse(
            notificationHistoryOneMock());
    public static NotificationHistoryRequest notificationHistoryRequest = new NotificationHistoryRequest(
            1,
            1L,
            1,
            "PENDING",
            "Test Content",
            "Channel one");

    public static List<NotificationHistoryResponse> notificationHistoryListResponse = List.of(
            notificationHistoryResponse,
            new NotificationHistoryResponse(notificationHistoryTwoMock()),
            new NotificationHistoryResponse(notificationHistoryThreeMock()));

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
                paymentInstallmentThreeMock());
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
            new PaymentInstallmentResponse(paymentInstallmentThreeMock()));

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

    public static List<PaymentInstallment> pendingInstallmentsListMock() {
        return List.of(
                paymentInstallmentOneMock(),
                pendingInstallmentMock());
    }

    public static List<PaymentInstallment> overdueInstallmentsListMock() {
        return List.of(
                paymentInstallmentTwoMock(),
                overdueInstallmentMock());
    }

    public static List<PaymentInstallment> paidInstallmentsListMock() {
        return List.of(
                paymentInstallmentThreeMock(),
                paidInstallmentMock());
    }

    public static List<PaymentInstallment> cancelledInstallmentsListMock() {
        return List.of(
                paymentInstallmentOneMock(),
                cancelledInstallmentMock());
    }

    /*
     * TourFaq instances
     * 
     * 
     */
    public static TourFaq tourFaqOneMock() {
        return TourFaq.builder()
                .id(1)
                .tourPlan(tourPlanOneMock)
                .question("¿Cuál es la duración del tour?")
                .answer("El tour tiene una duración de 4 horas aproximadamente, incluyendo traslados.")
                .displayOrder(1)
                .createdAt(LocalDateTime.now().minusDays(5))
                .updatedAt(LocalDateTime.now().minusDays(2))
                .build();
    }

    public static TourFaq tourFaqTwoMock() {
        return TourFaq.builder()
                .id(2)
                .tourPlan(tourPlanOneMock)
                .question("¿Qué incluye el precio del tour?")
                .answer("El precio incluye transporte, guía bilingüe, entradas a los sitios turísticos y refrigerio.")
                .displayOrder(2)
                .createdAt(LocalDateTime.now().minusDays(4))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();
    }

    public static TourFaq tourFaqThreeMock() {
        return TourFaq.builder()
                .id(3)
                .tourPlan(tourPlanTwoMock)
                .question("¿Cuál es el punto de encuentro?")
                .answer("El punto de encuentro es en el lobby del hotel o en la Plaza Mayor a las 8:00 AM.")
                .displayOrder(1)
                .createdAt(LocalDateTime.now().minusDays(3))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static TourFaq tourFaqFourMock() {
        return TourFaq.builder()
                .id(4)
                .tourPlan(tourPlanTwoMock)
                .question("¿Se puede cancelar el tour?")
                .answer("Sí, se puede cancelar hasta 24 horas antes del inicio del tour sin penalización.")
                .displayOrder(2)
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();
    }

    public static TourFaq tourFaqFiveMock() {
        return TourFaq.builder()
                .id(5)
                .tourPlan(tourPlanThreeMock)
                .question("¿Es necesario reservar con anticipación?")
                .answer("Se recomienda reservar con al menos 48 horas de anticipación para garantizar disponibilidad.")
                .displayOrder(1)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static List<TourFaq> tourFaqListMock() {
        return List.of(
                tourFaqOneMock(),
                tourFaqTwoMock(),
                tourFaqThreeMock());
    }

    public static TourFaqResponse tourFaqResponseMock = new TourFaqResponse(tourFaqOneMock());

    public static TourFaqRequest tourFaqRequestMock = new TourFaqRequest(
            1,
            "¿Cuál es la duración del tour?",
            "El tour tiene una duración de 4 horas aproximadamente, incluyendo traslados.",
            1);

    public static List<TourFaqResponse> tourFaqResponseListMock = List.of(
            new TourFaqResponse(tourFaqOneMock()),
            new TourFaqResponse(tourFaqTwoMock()),
            new TourFaqResponse(tourFaqThreeMock()));

    // Additional mock data for specific test scenarios
    public static TourFaq tourFaqWithHighDisplayOrderMock() {
        return TourFaq.builder()
                .id(6)
                .tourPlan(tourPlanOneMock)
                .question("¿Hay descuentos para grupos?")
                .answer("Sí, ofrecemos descuentos especiales para grupos de 10 o más personas.")
                .displayOrder(10)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static TourFaq tourFaqWithDuplicateQuestionMock() {
        return TourFaq.builder()
                .id(7)
                .tourPlan(tourPlanOneMock)
                .question("¿Cuál es la duración del tour?")
                .answer("Duración aproximada de 4 horas con traslados incluidos.")
                .displayOrder(3)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static TourFaq tourFaqWithDuplicateDisplayOrderMock() {
        return TourFaq.builder()
                .id(8)
                .tourPlan(tourPlanOneMock)
                .question("¿Qué ropa debo llevar?")
                .answer("Se recomienda ropa cómoda y zapatos cerrados para caminar.")
                .displayOrder(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static TourFaq tourFaqForDifferentTourPlanMock() {
        return TourFaq.builder()
                .id(9)
                .tourPlan(tourPlanThreeMock)
                .question("¿El tour es accesible para personas con movilidad reducida?")
                .answer("Sí, contamos con rutas accesibles y vehículos adaptados.")
                .displayOrder(2)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static TourFaq tourFaqWithLongQuestionMock() {
        return TourFaq.builder()
                .id(10)
                .tourPlan(tourPlanTwoMock)
                .question(
                        "¿Cuáles son las políticas de cancelación y reembolso en caso de condiciones climáticas adversas o eventos de fuerza mayor que puedan afectar la realización del tour?")
                .answer("En caso de condiciones climáticas adversas, ofrecemos reprogramación gratuita o reembolso completo.")
                .displayOrder(3)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static TourFaq tourFaqWithLongAnswerMock() {
        return TourFaq.builder()
                .id(11)
                .tourPlan(tourPlanThreeMock)
                .question("¿Qué incluye el tour?")
                .answer(
                        "El tour incluye transporte en vehículo climatizado desde y hacia su hotel, guía turístico certificado bilingüe (español e inglés), entradas a todos los sitios turísticos mencionados en el itinerario, refrigerio con bebidas y snacks locales, seguro de viajero durante la duración del tour, fotos profesionales del grupo (opcional), y certificado de participación. No incluye propinas para el guía y conductor, comidas principales, bebidas alcohólicas, gastos personales, ni servicios no mencionados en el itinerario.")
                .displayOrder(3)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // Lists for specific test scenarios
    public static List<TourFaq> tourFaqListForTourPlanOneMock() {
        return List.of(
                tourFaqOneMock(),
                tourFaqTwoMock(),
                tourFaqWithHighDisplayOrderMock());
    }

    public static List<TourFaq> tourFaqListForTourPlanTwoMock() {
        return List.of(
                tourFaqThreeMock(),
                tourFaqFourMock(),
                tourFaqWithLongQuestionMock());
    }

    public static List<TourFaq> tourFaqListForTourPlanThreeMock() {
        return List.of(
                tourFaqFiveMock(),
                tourFaqForDifferentTourPlanMock(),
                tourFaqWithLongAnswerMock());
    }

    public static List<TourFaq> tourFaqListOrderedByDisplayOrderMock() {
        return List.of(
                tourFaqOneMock(),
                tourFaqThreeMock(),
                tourFaqFiveMock(),
                tourFaqTwoMock(),
                tourFaqFourMock());
    }

    public static List<TourFaq> tourFaqListWithDuplicatesMock() {
        return List.of(
                tourFaqOneMock(),
                tourFaqWithDuplicateQuestionMock(),
                tourFaqWithDuplicateDisplayOrderMock());
    }

    // DTOs for specific test scenarios
    public static TourFaqRequest tourFaqRequestWithInvalidTourPlanMock() {
        return new TourFaqRequest(
                999, // Invalid tour plan ID
                "¿Es válida esta pregunta?",
                "Esta es una respuesta de prueba.",
                1);
    }

    public static TourFaqRequest tourFaqRequestWithDuplicateQuestionMock() {
        return new TourFaqRequest(
                1,
                "¿Cuál es la duración del tour?", // Duplicate question
                "Esta es una respuesta diferente.",
                3);
    }

    public static TourFaqRequest tourFaqRequestWithDuplicateDisplayOrderMock() {
        return new TourFaqRequest(
                1,
                "¿Nueva pregunta?",
                "Esta es una nueva respuesta.",
                1 // Duplicate display order
        );
    }

    public static TourFaqRequest tourFaqRequestWithShortQuestionMock() {
        return new TourFaqRequest(
                1,
                "¿Duración?", // Too short
                "Esta es una respuesta de prueba.",
                1);
    }

    public static TourFaqRequest tourFaqRequestWithShortAnswerMock() {
        return new TourFaqRequest(
                1,
                "¿Cuál es la duración del tour?",
                "4 horas.", // Too short
                1);
    }

    public static TourFaqRequest tourFaqRequestWithNegativeDisplayOrderMock() {
        return new TourFaqRequest(
                1,
                "¿Cuál es la duración del tour?",
                "El tour tiene una duración de 4 horas aproximadamente.",
                -1 // Negative display order
        );
    }

    // Response lists for specific test scenarios
    public static List<TourFaqResponse> tourFaqResponseListForTourPlanOneMock() {
        return List.of(
                new TourFaqResponse(tourFaqOneMock()),
                new TourFaqResponse(tourFaqTwoMock()),
                new TourFaqResponse(tourFaqWithHighDisplayOrderMock()));
    }

    public static List<TourFaqResponse> tourFaqResponseListOrderedMock() {
        return List.of(
                new TourFaqResponse(tourFaqOneMock()),
                new TourFaqResponse(tourFaqThreeMock()),
                new TourFaqResponse(tourFaqFiveMock()));
    }

    public static List<TourFaqResponse> emptyTourFaqResponseListMock() {
        return List.of();
    }

    // Request lists for bulk operations
    public static List<TourFaqRequest> tourFaqRequestListForBulkCreateMock() {
        return List.of(
                new TourFaqRequest(1, "¿Pregunta 1?", "Respuesta 1", 1),
                new TourFaqRequest(1, "¿Pregunta 2?", "Respuesta 2", 2),
                new TourFaqRequest(1, "¿Pregunta 3?", "Respuesta 3", 3));
    }

    public static List<TourFaqRequest> tourFaqRequestListForBulkUpdateMock() {
        return List.of(
                new TourFaqRequest(1, "¿Pregunta actualizada 1?", "Respuesta actualizada 1", 1),
                new TourFaqRequest(1, "¿Pregunta actualizada 2?", "Respuesta actualizada 2", 2));
    }

    public static List<Integer> tourFaqIdsForBulkDeleteMock() {
        return List.of(1, 2, 3, 4, 5);
    }

    public static List<Integer> tourFaqIdsForReorderMock() {
        return List.of(3, 1, 2, 5, 4);
    }

    /*
     * TourPriceHistory instances
     * 
     * 
     */

    public static TourPriceHistory tourPriceHistoryOneMock() {
        return TourPriceHistory.builder()
                .id(1)
                .tourPlan(tourPlanOneMock)
                .previousPrice(BigDecimal.valueOf(100.00))
                .newPrice(BigDecimal.valueOf(120.00))
                .changedAt(LocalDateTime.now().minusDays(5))
                .changedBy(userAdmin())
                .reason("Price increase due to seasonal demand")
                .build();
    }

    public static TourPriceHistory tourPriceHistoryTwoMock() {
        return TourPriceHistory.builder()
                .id(2)
                .tourPlan(tourPlanOneMock)
                .previousPrice(BigDecimal.valueOf(120.00))
                .newPrice(BigDecimal.valueOf(110.00))
                .changedAt(LocalDateTime.now().minusDays(3))
                .changedBy(userOperator())
                .reason("Promotional discount")
                .build();
    }

    public static TourPriceHistory tourPriceHistoryThreeMock() {
        return TourPriceHistory.builder()
                .id(3)
                .tourPlan(tourPlanTwoMock)
                .previousPrice(BigDecimal.valueOf(200.00))
                .newPrice(BigDecimal.valueOf(250.00))
                .changedAt(LocalDateTime.now().minusDays(2))
                .changedBy(userAdmin())
                .reason("Updated pricing strategy")
                .build();
    }

    public static List<TourPriceHistory> tourPriceHistoryListMock() {
        return List.of(
                tourPriceHistoryOneMock(),
                tourPriceHistoryTwoMock(),
                tourPriceHistoryThreeMock());
    }

    public static TourPriceHistoryResponse tourPriceHistoryResponseMock = new TourPriceHistoryResponse(
            tourPriceHistoryOneMock());

    public static TourPriceHistoryRequest tourPriceHistoryRequestMock = new TourPriceHistoryRequest(
            1,
            BigDecimal.valueOf(100.00),
            BigDecimal.valueOf(120.00),
            1L,
            "Test price change");

    public static List<TourPriceHistoryResponse> tourPriceHistoryResponseListMock = List.of(
            new TourPriceHistoryResponse(tourPriceHistoryOneMock()),
            new TourPriceHistoryResponse(tourPriceHistoryTwoMock()),
            new TourPriceHistoryResponse(tourPriceHistoryThreeMock()));

    // Additional mock data for specific test scenarios
    public static TourPriceHistory tourPriceHistoryForTourPlanOneMock() {
        return TourPriceHistory.builder()
                .id(7)
                .tourPlan(tourPlanOneMock)
                .previousPrice(BigDecimal.valueOf(110.00))
                .newPrice(BigDecimal.valueOf(95.00))
                .changedAt(LocalDateTime.now().minusDays(10))
                .changedBy(userCustomer())
                .reason("Customer feedback pricing adjustment")
                .build();
    }

    public static TourPriceHistory tourPriceHistoryForTourPlanTwoMock() {
        return TourPriceHistory.builder()
                .id(8)
                .tourPlan(tourPlanTwoMock)
                .previousPrice(BigDecimal.valueOf(230.00))
                .newPrice(BigDecimal.valueOf(270.00))
                .changedAt(LocalDateTime.now().minusDays(7))
                .changedBy(userOperator())
                .reason("Inflation adjustment")
                .build();
    }

    public static TourPriceHistory tourPriceHistoryWithNoUserMock() {
        return TourPriceHistory.builder()
                .id(9)
                .tourPlan(tourPlanThreeMock)
                .previousPrice(BigDecimal.valueOf(320.00))
                .newPrice(BigDecimal.valueOf(300.00))
                .changedAt(LocalDateTime.now().minusDays(4))
                .changedBy(null)
                .reason("System automatic price adjustment")
                .build();
    }

    public static TourPriceHistory tourPriceHistoryWithNoReasonMock() {
        return TourPriceHistory.builder()
                .id(10)
                .tourPlan(tourPlanOneMock)
                .previousPrice(BigDecimal.valueOf(95.00))
                .newPrice(BigDecimal.valueOf(105.00))
                .changedAt(LocalDateTime.now().minusDays(1))
                .changedBy(userAdmin())
                .reason(null)
                .build();
    }

    public static TourPriceHistory tourPriceHistoryRecentMock() {
        return TourPriceHistory.builder()
                .id(11)
                .tourPlan(tourPlanTwoMock)
                .previousPrice(BigDecimal.valueOf(270.00))
                .newPrice(BigDecimal.valueOf(260.00))
                .changedAt(LocalDateTime.now().minusMinutes(30))
                .changedBy(userAdmin())
                .reason("Last minute promotional offer")
                .build();
    }

    public static TourPriceHistory tourPriceHistoryOldMock() {
        return TourPriceHistory.builder()
                .id(12)
                .tourPlan(tourPlanThreeMock)
                .previousPrice(BigDecimal.valueOf(300.00))
                .newPrice(BigDecimal.valueOf(290.00))
                .changedAt(LocalDateTime.now().minusDays(30))
                .changedBy(userContentManager())
                .reason("Monthly pricing review")
                .build();
    }

    public static TourPriceHistory tourPriceHistorySamePriceMock() {
        return TourPriceHistory.builder()
                .id(13)
                .tourPlan(tourPlanOneMock)
                .previousPrice(BigDecimal.valueOf(105.00))
                .newPrice(BigDecimal.valueOf(105.00))
                .changedAt(LocalDateTime.now().minusHours(2))
                .changedBy(userSupport())
                .reason("Price verification - no change needed")
                .build();
    }

    public static TourPriceHistory tourPriceHistoryLargeIncreaseMock() {
        return TourPriceHistory.builder()
                .id(14)
                .tourPlan(tourPlanTwoMock)
                .previousPrice(BigDecimal.valueOf(260.00))
                .newPrice(BigDecimal.valueOf(350.00))
                .changedAt(LocalDateTime.now().minusHours(1))
                .changedBy(userAdmin())
                .reason("Premium service upgrade")
                .build();
    }

    public static TourPriceHistory tourPriceHistoryLargeDecreaseMock() {
        return TourPriceHistory.builder()
                .id(15)
                .tourPlan(tourPlanThreeMock)
                .previousPrice(BigDecimal.valueOf(290.00))
                .newPrice(BigDecimal.valueOf(200.00))
                .changedAt(LocalDateTime.now().minusMinutes(15))
                .changedBy(userOperator())
                .reason("Emergency clearance sale")
                .build();
    }

    // Lists for specific test scenarios
    public static List<TourPriceHistory> tourPriceHistoryListForTourPlanOneMock() {
        return List.of(
                tourPriceHistoryOneMock(),
                tourPriceHistoryTwoMock(),
                tourPriceHistoryForTourPlanOneMock(),
                tourPriceHistoryWithNoReasonMock(),
                tourPriceHistorySamePriceMock());
    }

    public static List<TourPriceHistory> tourPriceHistoryListForTourPlanTwoMock() {
        return List.of(
                tourPriceHistoryThreeMock(),
                tourPriceHistoryForTourPlanTwoMock(),
                tourPriceHistoryRecentMock(),
                tourPriceHistoryLargeIncreaseMock());
    }

    public static List<TourPriceHistory> tourPriceHistoryListForTourPlanThreeMock() {
        return List.of(
                tourPriceHistoryWithNoUserMock(),
                tourPriceHistoryOldMock(),
                tourPriceHistoryLargeDecreaseMock());
    }

    public static List<TourPriceHistory> tourPriceHistoryListOrderedByDateMock() {
        return List.of(
                tourPriceHistoryOldMock(),
                tourPriceHistoryWithNoUserMock(),
                tourPriceHistoryForTourPlanTwoMock(),
                tourPriceHistoryForTourPlanOneMock(),
                tourPriceHistoryWithNoReasonMock(),
                tourPriceHistoryRecentMock(),
                tourPriceHistoryLargeIncreaseMock(),
                tourPriceHistoryLargeDecreaseMock());
    }

    public static List<TourPriceHistory> tourPriceHistoryListByUserMock() {
        return List.of(
                tourPriceHistoryOneMock(),
                tourPriceHistoryThreeMock(),
                tourPriceHistoryRecentMock(),
                tourPriceHistoryLargeIncreaseMock());
    }

    public static List<TourPriceHistory> tourPriceHistoryListRecentMock() {
        return List.of(
                tourPriceHistoryRecentMock(),
                tourPriceHistoryLargeIncreaseMock(),
                tourPriceHistoryLargeDecreaseMock(),
                tourPriceHistoryWithNoReasonMock(),
                tourPriceHistorySamePriceMock());
    }

    // DTOs for specific test scenarios
    public static TourPriceHistoryRequest tourPriceHistoryRequestWithInvalidTourPlanMock() {
        return new TourPriceHistoryRequest(
                999, // Invalid tour plan ID
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(120.00),
                1L,
                "Test price change");
    }

    public static TourPriceHistoryRequest tourPriceHistoryRequestWithInvalidUserMock() {
        return new TourPriceHistoryRequest(
                1,
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(120.00),
                999L, // Invalid user ID
                "Test price change");
    }

    public static TourPriceHistoryRequest tourPriceHistoryRequestWithNegativePricesMock() {
        return new TourPriceHistoryRequest(
                1,
                BigDecimal.valueOf(-50.00), // Negative previous price
                BigDecimal.valueOf(120.00),
                1L,
                "Invalid price test");
    }

    public static TourPriceHistoryRequest tourPriceHistoryRequestWithZeroPricesMock() {
        return new TourPriceHistoryRequest(
                1,
                BigDecimal.valueOf(0.00),
                BigDecimal.valueOf(0.00),
                1L,
                "Zero price test");
    }

    public static TourPriceHistoryRequest tourPriceHistoryRequestWithLongReasonMock() {
        return new TourPriceHistoryRequest(
                1,
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(120.00),
                1L,
                "This is a very long reason that exceeds the maximum allowed length for the reason field and should be validated properly in the system to ensure data integrity and proper user experience when entering price change reasons in the tour management application");
    }

    public static TourPriceHistoryRequest tourPriceHistoryRequestWithSamePricesMock() {
        return new TourPriceHistoryRequest(
                1,
                BigDecimal.valueOf(120.00),
                BigDecimal.valueOf(120.00),
                1L,
                "Same price test");
    }

    // Response lists for specific test scenarios
    public static List<TourPriceHistoryResponse> tourPriceHistoryResponseListForTourPlanOneMock() {
        return List.of(
                new TourPriceHistoryResponse(tourPriceHistoryOneMock()),
                new TourPriceHistoryResponse(tourPriceHistoryTwoMock()),
                new TourPriceHistoryResponse(tourPriceHistoryForTourPlanOneMock()),
                new TourPriceHistoryResponse(tourPriceHistoryWithNoReasonMock()),
                new TourPriceHistoryResponse(tourPriceHistorySamePriceMock()));
    }

    public static List<TourPriceHistoryResponse> tourPriceHistoryResponseListOrderedMock() {
        return List.of(
                new TourPriceHistoryResponse(tourPriceHistoryOldMock()),
                new TourPriceHistoryResponse(tourPriceHistoryWithNoUserMock()),
                new TourPriceHistoryResponse(tourPriceHistoryForTourPlanTwoMock()),
                new TourPriceHistoryResponse(tourPriceHistoryRecentMock()));
    }

    public static List<TourPriceHistoryResponse> emptyTourPriceHistoryResponseListMock() {
        return List.of();
    }

    // Request lists for bulk operations
    public static List<TourPriceHistoryRequest> tourPriceHistoryRequestListForBulkCreateMock() {
        return List.of(
                new TourPriceHistoryRequest(1, BigDecimal.valueOf(105.00), BigDecimal.valueOf(115.00), 1L,
                        "Bulk create 1"),
                new TourPriceHistoryRequest(2, BigDecimal.valueOf(230.00), BigDecimal.valueOf(240.00), 2L,
                        "Bulk create 2"),
                new TourPriceHistoryRequest(3, BigDecimal.valueOf(320.00), BigDecimal.valueOf(310.00), 3L,
                        "Bulk create 3"));
    }

    public static List<TourPriceHistoryRequest> tourPriceHistoryRequestListForBulkUpdateMock() {
        return List.of(
                new TourPriceHistoryRequest(1, BigDecimal.valueOf(115.00), BigDecimal.valueOf(125.00), 1L,
                        "Bulk update 1"),
                new TourPriceHistoryRequest(2, BigDecimal.valueOf(240.00), BigDecimal.valueOf(250.00), 2L,
                        "Bulk update 2"));
    }

    public static List<Integer> tourPriceHistoryIdsForBulkDeleteMock() {
        return List.of(1, 2, 3, 4, 5, 6);
    }

    // Statistics and analytics test data
    public static List<TourPriceHistory> tourPriceHistoryListForStatisticsMock() {
        return List.of(
                // Tour Plan 1: 100 -> 120 -> 110 -> 95 -> 105
                TourPriceHistory.builder().id(1).tourPlan(tourPlanOneMock).previousPrice(BigDecimal.valueOf(100.00))
                        .newPrice(BigDecimal.valueOf(120.00)).changedAt(LocalDateTime.now().minusDays(10))
                        .changedBy(userAdmin()).reason("Increase").build(),
                TourPriceHistory.builder().id(2).tourPlan(tourPlanOneMock).previousPrice(BigDecimal.valueOf(120.00))
                        .newPrice(BigDecimal.valueOf(110.00)).changedAt(LocalDateTime.now().minusDays(8))
                        .changedBy(userOperator()).reason("Decrease").build(),
                TourPriceHistory.builder().id(3).tourPlan(tourPlanOneMock).previousPrice(BigDecimal.valueOf(110.00))
                        .newPrice(BigDecimal.valueOf(95.00)).changedAt(LocalDateTime.now().minusDays(5))
                        .changedBy(userAdmin()).reason("Decrease").build(),
                TourPriceHistory.builder().id(4).tourPlan(tourPlanOneMock).previousPrice(BigDecimal.valueOf(95.00))
                        .newPrice(BigDecimal.valueOf(105.00)).changedAt(LocalDateTime.now().minusDays(2))
                        .changedBy(userOperator()).reason("Increase").build(),

                // Tour Plan 2: 200 -> 250 -> 230 -> 270 -> 260
                TourPriceHistory.builder().id(5).tourPlan(tourPlanTwoMock).previousPrice(BigDecimal.valueOf(200.00))
                        .newPrice(BigDecimal.valueOf(250.00)).changedAt(LocalDateTime.now().minusDays(12))
                        .changedBy(userAdmin()).reason("Increase").build(),
                TourPriceHistory.builder().id(6).tourPlan(tourPlanTwoMock).previousPrice(BigDecimal.valueOf(250.00))
                        .newPrice(BigDecimal.valueOf(230.00)).changedAt(LocalDateTime.now().minusDays(9))
                        .changedBy(userContentManager()).reason("Decrease").build(),
                TourPriceHistory.builder().id(7).tourPlan(tourPlanTwoMock).previousPrice(BigDecimal.valueOf(230.00))
                        .newPrice(BigDecimal.valueOf(270.00)).changedAt(LocalDateTime.now().minusDays(6))
                        .changedBy(userOperator()).reason("Increase").build(),
                TourPriceHistory.builder().id(8).tourPlan(tourPlanTwoMock).previousPrice(BigDecimal.valueOf(270.00))
                        .newPrice(BigDecimal.valueOf(260.00)).changedAt(LocalDateTime.now().minusDays(3))
                        .changedBy(userAdmin()).reason("Decrease").build());
    }

    /*
     * AuditLog instances
     * Basic instances for AuditLog testing
     */

    public static AuditLog auditLogOneMock() {
        return AuditLog.builder()
                .id(1)
                .entityType("User")
                .entityId(1)
                .action("CREATE")
                .user(userAdmin())
                .actionTimestamp(LocalDateTime.now().minusHours(2))
                .oldValues(null)
                .newValues("{\"name\":\"admin\",\"email\":\"admin@example.com\"}")
                .ipAddress("192.168.1.1")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .build();
    }

    public static AuditLog auditLogTwoMock() {
        return AuditLog.builder()
                .id(2)
                .entityType("Reservation")
                .entityId(1)
                .action("UPDATE")
                .user(userOperator())
                .actionTimestamp(LocalDateTime.now().minusHours(1))
                .oldValues("{\"status\":\"pending\"}")
                .newValues("{\"status\":\"confirmed\"}")
                .ipAddress("192.168.1.2")
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)")
                .build();
    }

    public static AuditLog auditLogThreeMock() {
        return AuditLog.builder()
                .id(3)
                .entityType("Payment")
                .entityId(1)
                .action("DELETE")
                .user(userCustomer())
                .actionTimestamp(LocalDateTime.now().minusMinutes(30))
                .oldValues("{\"amount\":\"100.00\",\"status\":\"pending\"}")
                .newValues(null)
                .ipAddress("192.168.1.3")
                .userAgent("Mozilla/5.0 (X11; Linux x86_64)")
                .build();
    }

    public static AuditLog auditLogFourMock() {
        return AuditLog.builder()
                .id(4)
                .entityType("TourPlan")
                .entityId(2)
                .action("READ")
                .user(userSupport())
                .actionTimestamp(LocalDateTime.now().minusMinutes(15))
                .oldValues(null)
                .newValues(null)
                .ipAddress("192.168.1.4")
                .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 14_0)")
                .build();
    }

    public static AuditLog auditLogFiveMock() {
        return AuditLog.builder()
                .id(5)
                .entityType("User")
                .entityId(2)
                .action("LOGIN_SUCCESS")
                .user(userContentManager())
                .actionTimestamp(LocalDateTime.now().minusMinutes(5))
                .oldValues(null)
                .newValues("{\"loginTime\":\"" + LocalDateTime.now().minusMinutes(5) + "\"}")
                .ipAddress("192.168.1.5")
                .userAgent("Mozilla/5.0 (Android 10; Mobile)")
                .build();
    }

    public static List<AuditLog> auditLogListMock() {
        return List.of(
                auditLogOneMock(),
                auditLogTwoMock(),
                auditLogThreeMock(),
                auditLogFourMock(),
                auditLogFiveMock());
    }

    // Additional mock data for specific test scenarios
    public static AuditLog auditLogWithNullUserMock() {
        return AuditLog.builder()
                .id(6)
                .entityType("System")
                .entityId(1)
                .action("SYSTEM_START")
                .user(null)
                .actionTimestamp(LocalDateTime.now().minusDays(1))
                .oldValues(null)
                .newValues("{\"startup\":\"successful\"}")
                .ipAddress("127.0.0.1")
                .userAgent("System")
                .build();
    }

    public static AuditLog auditLogWithNullIpMock() {
        return AuditLog.builder()
                .id(7)
                .entityType("Reservation")
                .entityId(3)
                .action("AUTO_CANCEL")
                .user(userOperator())
                .actionTimestamp(LocalDateTime.now().minusHours(6))
                .oldValues("{\"status\":\"pending\"}")
                .newValues("{\"status\":\"cancelled\"}")
                .ipAddress(null)
                .userAgent("System/1.0")
                .build();
    }

    public static AuditLog auditLogWithEmptyValuesMock() {
        return AuditLog.builder()
                .id(8)
                .entityType("TourPlan")
                .entityId(3)
                .action("VIEW")
                .user(userCustomer())
                .actionTimestamp(LocalDateTime.now().minusMinutes(10))
                .oldValues("")
                .newValues("")
                .ipAddress("192.168.1.6")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .build();
    }

    public static AuditLog auditLogFailedLoginMock() {
        return AuditLog.builder()
                .id(9)
                .entityType("User")
                .entityId(3)
                .action("LOGIN_FAILED")
                .user(null)
                .actionTimestamp(LocalDateTime.now().minusMinutes(2))
                .oldValues(null)
                .newValues("{\"attempts\":3}")
                .ipAddress("192.168.1.7")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .build();
    }

    public static AuditLog auditLogOldMock() {
        return AuditLog.builder()
                .id(10)
                .entityType("Payment")
                .entityId(2)
                .action("REFUND")
                .user(userAdmin())
                .actionTimestamp(LocalDateTime.now().minusDays(30))
                .oldValues("{\"amount\":\"200.00\",\"status\":\"completed\"}")
                .newValues("{\"amount\":\"200.00\",\"status\":\"refunded\"}")
                .ipAddress("192.168.1.8")
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6)")
                .build();
    }

    // Lists for specific test scenarios
    public static List<AuditLog> auditLogListByUserMock() {
        return List.of(
                auditLogOneMock(),
                auditLogFiveMock());
    }

    public static List<AuditLog> auditLogListByEntityTypeMock() {
        return List.of(
                auditLogTwoMock(),
                auditLogFourMock(),
                auditLogWithEmptyValuesMock());
    }

    public static List<AuditLog> auditLogListByActionMock() {
        return List.of(
                auditLogOneMock(),
                auditLogFiveMock());
    }

    public static List<AuditLog> auditLogListByDateRangeMock() {
        return List.of(
                auditLogOneMock(),
                auditLogTwoMock(),
                auditLogThreeMock(),
                auditLogFourMock(),
                auditLogFiveMock());
    }

    public static List<AuditLog> auditLogListWithNullsMock() {
        return List.of(
                auditLogWithNullUserMock(),
                auditLogWithNullIpMock(),
                auditLogWithEmptyValuesMock());
    }

    public static List<AuditLog> auditLogListSecurityMock() {
        return List.of(
                auditLogFiveMock(),
                auditLogFailedLoginMock());
    }

    public static List<AuditLog> auditLogListRecentMock() {
        return List.of(
                auditLogThreeMock(),
                auditLogFourMock(),
                auditLogFiveMock());
    }

    public static List<AuditLog> auditLogListOldMock() {
        return List.of(
                auditLogOldMock(),
                auditLogWithNullUserMock());
    }

    // Request and Response DTOs (if needed for testing)
    public static List<Integer> auditLogIdsForBulkDeleteMock() {
        return List.of(1, 2, 3, 4, 5);
    }

    public static List<AuditLog> auditLogListForBulkCreateMock() {
        return List.of(
                AuditLog.builder()
                        .entityType("Test")
                        .entityId(100)
                        .action("CREATE")
                        .user(userAdmin())
                        .actionTimestamp(LocalDateTime.now())
                        .oldValues(null)
                        .newValues("{\"test\":\"data\"}")
                        .ipAddress("192.168.1.100")
                        .userAgent("Test Agent")
                        .build(),
                AuditLog.builder()
                        .entityType("Test")
                        .entityId(101)
                        .action("UPDATE")
                        .user(userOperator())
                        .actionTimestamp(LocalDateTime.now())
                        .oldValues("{\"old\":\"value\"}")
                        .newValues("{\"new\":\"value\"}")
                        .ipAddress("192.168.1.101")
                        .userAgent("Test Agent")
                        .build());
    }

    /*
     * ReviewCategory instances
     * 
     * 
     */

    public static ReviewCategory reviewCategoryOneMock() {
        return ReviewCategory.builder()
                .name("Quality")
                .description("Quality of the tour")
                .build();
    }

    public static ReviewCategory reviewCategoryTwoMock() {
        return ReviewCategory.builder()
                .name("Price")
                .description("Price of the tour")
                .build();
    }

    public static ReviewCategory reviewCategoryThreeMock() {
        return ReviewCategory.builder()
                .name("Location")
                .description("Location of the tour")
                .build();
    }

    public static List<ReviewCategory> reviewCategoryListMock() {
        return List.of(
                reviewCategoryOneMock(),
                reviewCategoryTwoMock(),
                reviewCategoryThreeMock());
    }

    public static ReviewCategoryResponse reviewCategoryResponseMock() {
        return new ReviewCategoryResponse(reviewCategoryOneMock());
    }

    public static ReviewCategoryRequest reviewCategoryRequestMock() {
        return new ReviewCategoryRequest("Quality", "Quality of the tour");
    }

    public static List<ReviewCategoryResponse> reviewCategoryResponseListMock() {
        return List.of(
                new ReviewCategoryResponse(reviewCategoryOneMock()),
                new ReviewCategoryResponse(reviewCategoryTwoMock()),
                new ReviewCategoryResponse(reviewCategoryThreeMock()));
    }

    /*
     * Review instances
     */

    public static Review reviewOneMock() {
        Review review = new Review();
        review.setId(1L);
        review.setUserId(userCustomer());
        review.setTourPlanId(tourPlanOneMock);
        review.setTitle("Great tour");
        review.setRating(5);
        review.setComment("Excellent experience");
        review.setVerifiedPurchase(true);
        review.setStatus("ACTIVE");
        review.setCreatedAt(LocalDateTime.now());
        return review;
    }

    /*
     * ReviewCategoryRating instances
     */

    public static ReviewCategoryRating reviewCategoryRatingOneMock() {
        ReviewCategoryRatingId id = ReviewCategoryRatingId.builder()
                .reviewId(1)
                .categoryId(1)
                .build();
        return ReviewCategoryRating.builder()
                .id(id)
                .review(reviewOneMock())
                .category(reviewCategoryOneMock())
                .rating(4)
                .build();
    }

    public static ReviewCategoryRating reviewCategoryRatingTwoMock() {
        ReviewCategoryRatingId id = ReviewCategoryRatingId.builder()
                .reviewId(2)
                .categoryId(2)
                .build();
        return ReviewCategoryRating.builder()
                .id(id)
                .review(reviewOneMock())
                .category(reviewCategoryOneMock())
                .rating(2)
                .build();
      }
    
     
}
