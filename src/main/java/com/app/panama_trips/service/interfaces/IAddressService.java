package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.AddressRequest;
import com.app.panama_trips.presentation.dto.AddressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Contrato de servicio para la gestión de direcciones.
 */
public interface IAddressService {

    /**
     * Obtiene todas las direcciones con paginación.
     * @param pageable configuración de paginación
     * @return página de direcciones
     */
    Page<AddressResponse> getAllAddresses(Pageable pageable);

    /**
     * Obtiene una dirección por su ID.
     * @param id identificador de la dirección
     * @return la dirección encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    AddressResponse getAddressById(Integer id);

    /**
     * Obtiene una dirección por nombre de calle.
     * @param street nombre de la calle
     * @return la dirección encontrada
     */
    AddressResponse getAddressByStreet(String street);

    /**
     * Obtiene todas las direcciones de un distrito.
     * @param districtId identificador del distrito
     * @return lista de direcciones del distrito
     */
    List<AddressResponse> getAddressesByDistrictId(Integer districtId);

    /**
     * Obtiene direcciones por código postal.
     * @param postalCode código postal a buscar
     * @return lista de direcciones con ese código postal
     */
    List<AddressResponse> getAddressesByPostalCode(String postalCode);

    /**
     * Busca direcciones cuya calle contenga el fragmento indicado (sin distinción de mayúsculas).
     * @param streetFragment fragmento de texto a buscar
     * @return lista de direcciones coincidentes
     */
    List<AddressResponse> getAddressesByStreetContainingIgnoreCase(String streetFragment);

    /**
     * Crea una nueva dirección.
     * @param address datos de la dirección a crear
     * @return la dirección creada
     */
    AddressResponse saveAddress(AddressRequest address);

    /**
     * Actualiza una dirección existente.
     * @param id identificador de la dirección a actualizar
     * @param address nuevos datos de la dirección
     * @return la dirección actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    AddressResponse updateAddress(Integer id, AddressRequest address);

    /**
     * Elimina una dirección por su ID.
     * @param id identificador de la dirección a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    void deleteAddress(Integer id);
}
