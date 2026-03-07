package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Provider;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer> {
    Optional<Provider> findByRuc(String ruc);

    Optional<Provider> findByName(String name);

    Optional<Provider> findByEmail(String email);

    Optional<Provider> findByPhone(String phone);

    @EntityGraph(attributePaths = { "province", "district", "address" })
    List<Provider> findProvidersByProvince_Id(Integer provinceId);

    @EntityGraph(attributePaths = { "province", "district", "address" })
    List<Provider> findProvidersByDistrict_Id(Integer districtId);

    @EntityGraph(attributePaths = { "province", "district", "address" })
    List<Provider> findProvidersByAddress_AddressId(Integer addressAddressId);

    @EntityGraph(attributePaths = { "province", "district", "address" })
    List<Provider> findProvidersByNameContainingIgnoreCase(String name);

    boolean existsProviderByRuc(String ruc);

    boolean existsProviderByEmail(String email);
}
