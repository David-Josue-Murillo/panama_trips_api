package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Address;
import com.app.panama_trips.persistence.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findByStreet(String street);

    List<Address> findByDistrictId(District districtId);
    List<Address> findAddressesByPostalCode(Integer postalCode);

    @Query("SELECT a FROM Address a WHERE LOWER(a.street) LIKE LOWER(concat('%', :streetFragment, '%'))")
    List<Address> findByStreetContainingIgnoreCase(@Param("streetFragment") String streetFragment);
}
