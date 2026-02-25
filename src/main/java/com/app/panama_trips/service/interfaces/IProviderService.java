package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.ProviderRequest;
import com.app.panama_trips.presentation.dto.ProviderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Contrato de servicio para la gestion de proveedores de tours.
 * Provee operaciones CRUD y busquedas por identificadores unicos, ubicacion y nombre.
 */
public interface IProviderService {

    // CRUD operations

    /**
     * Obtiene todos los proveedores de forma paginada.
     *
     * @param pageable configuracion de paginacion
     * @return pagina de proveedores
     */
    Page<ProviderResponse> getAllProviders(Pageable pageable);

    /**
     * Obtiene un proveedor por su identificador.
     *
     * @param id identificador del proveedor
     * @return el proveedor encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el proveedor no existe
     */
    ProviderResponse getProviderById(Integer id);

    /**
     * Registra un nuevo proveedor.
     *
     * @param provider datos del proveedor a crear
     * @return el proveedor creado
     */
    ProviderResponse saveProvider(ProviderRequest provider);

    /**
     * Actualiza un proveedor existente.
     *
     * @param id identificador del proveedor
     * @param provider datos actualizados del proveedor
     * @return el proveedor actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el proveedor no existe
     */
    ProviderResponse updateProvider(Integer id, ProviderRequest provider);

    /**
     * Elimina un proveedor por su identificador.
     *
     * @param id identificador del proveedor
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el proveedor no existe
     */
    void deleteProvider(Integer id);

    // Additional service methods

    /**
     * Busca un proveedor por su RUC.
     *
     * @param ruc numero de RUC del proveedor
     * @return el proveedor encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    ProviderResponse getProviderByRuc(String ruc);

    /**
     * Busca un proveedor por su nombre exacto.
     *
     * @param name nombre del proveedor
     * @return el proveedor encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    ProviderResponse getProviderByName(String name);

    /**
     * Busca un proveedor por su correo electronico.
     *
     * @param email correo electronico del proveedor
     * @return el proveedor encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    ProviderResponse getProviderByEmail(String email);

    /**
     * Busca un proveedor por su numero de telefono.
     *
     * @param phone numero de telefono del proveedor
     * @return el proveedor encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    ProviderResponse getProviderByPhone(String phone);

    /**
     * Obtiene proveedores ubicados en una provincia especifica.
     *
     * @param provinceId identificador de la provincia
     * @return lista de proveedores en la provincia
     */
    List<ProviderResponse> getProvidersByProvinceId(Integer provinceId);

    /**
     * Obtiene proveedores ubicados en un distrito especifico.
     *
     * @param districtId identificador del distrito
     * @return lista de proveedores en el distrito
     */
    List<ProviderResponse> getProvidersByDistrictId(Integer districtId);

    /**
     * Obtiene proveedores asociados a una direccion especifica.
     *
     * @param addressId identificador de la direccion
     * @return lista de proveedores en la direccion
     */
    List<ProviderResponse> getProvidersByAddressId(Integer addressId);

    /**
     * Busca proveedores cuyo nombre contenga el fragmento indicado.
     *
     * @param nameFragment fragmento del nombre a buscar
     * @return lista de proveedores que coinciden
     */
    List<ProviderResponse> getProvidersByNameFragment(String nameFragment);

    /**
     * Verifica si existe un proveedor con el correo electronico dado.
     *
     * @param email correo electronico a verificar
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsProviderByEmail(String email);

    /**
     * Verifica si existe un proveedor con el RUC dado.
     *
     * @param ruc numero de RUC a verificar
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsProviderByRuc(String ruc);

    /**
     * Cuenta el numero total de proveedores registrados.
     *
     * @return cantidad total de proveedores
     */
    long countProviders();
}
