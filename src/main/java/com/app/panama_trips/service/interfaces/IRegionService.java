package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.RegionRequest;
import com.app.panama_trips.presentation.dto.RegionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contrato de servicio para la gestión de regiones turísticas.
 */
public interface IRegionService {

    /**
     * Obtiene todas las regiones con paginación.
     * @param pageable configuración de paginación
     * @return página de regiones
     */
    Page<RegionResponse> getAllRegions(Pageable pageable);

    /**
     * Obtiene una región por su ID.
     * @param id identificador de la región
     * @return la región encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    RegionResponse getRegionById(Integer id);

    /**
     * Crea una nueva región.
     * @param regionRequest datos de la región a crear
     * @return la región creada
     */
    RegionResponse saveRegion(RegionRequest regionRequest);

    /**
     * Actualiza una región existente.
     * @param id identificador de la región a actualizar
     * @param regionRequest nuevos datos de la región
     * @return la región actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    RegionResponse updateRegion(Integer id, RegionRequest regionRequest);

    /**
     * Elimina una región por su ID.
     * @param id identificador de la región a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    void deleteRegion(Integer id);

    /**
     * Obtiene una región por su nombre.
     * @param name nombre de la región
     * @return la región encontrada
     */
    RegionResponse getRegionByName(String name);

    /**
     * Busca regiones por nombre con paginación.
     * @param name nombre o fragmento a buscar
     * @param pageable configuración de paginación
     * @return página de regiones coincidentes
     */
    Page<RegionResponse> getRegionsByName(String name, Pageable pageable);

    /**
     * Obtiene regiones por provincia con paginación.
     * @param provinceId identificador de la provincia
     * @param pageable configuración de paginación
     * @return página de regiones de la provincia
     */
    Page<RegionResponse> getRegionByProvinceId(Integer provinceId, Pageable pageable);

    /**
     * Obtiene regiones por comarca con paginación.
     * @param comarcaId identificador de la comarca
     * @param pageable configuración de paginación
     * @return página de regiones de la comarca
     */
    Page<RegionResponse> getRegionByComarcaId(Integer comarcaId, Pageable pageable);

    /**
     * Cuenta el total de regiones registradas.
     * @return cantidad total de regiones
     */
    Long countRegions();

    /**
     * Verifica si existe una región con el nombre indicado.
     * @param name nombre a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsRegionByName(String name);
}
