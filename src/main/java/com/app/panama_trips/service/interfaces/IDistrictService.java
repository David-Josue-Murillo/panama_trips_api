package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.DistrictRequest;
import com.app.panama_trips.presentation.dto.DistrictResponse;

import java.util.List;

/**
 * Contrato de servicio para la gestión de distritos.
 */
public interface IDistrictService {

    /**
     * Obtiene todos los distritos.
     * @return lista de distritos
     */
    List<DistrictResponse> getAllDistricts();

    /**
     * Obtiene un distrito por su ID.
     * @param id identificador del distrito
     * @return el distrito encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    DistrictResponse getDistrictById(Integer id);

    /**
     * Obtiene un distrito por su nombre.
     * @param name nombre del distrito
     * @return el distrito encontrado
     */
    DistrictResponse getDistrictByName(String name);

    /**
     * Obtiene todos los distritos de una provincia.
     * @param provinceId identificador de la provincia
     * @return lista de distritos de la provincia
     */
    List<DistrictResponse> getDistrictsByProvinceId(Integer provinceId);

    /**
     * Crea un nuevo distrito.
     * @param districtRequest datos del distrito a crear
     * @return el distrito creado
     */
    DistrictResponse saveDistrict(DistrictRequest districtRequest);

    /**
     * Actualiza un distrito existente.
     * @param id identificador del distrito a actualizar
     * @param districtRequest nuevos datos del distrito
     * @return el distrito actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    DistrictResponse updateDistrict(Integer id, DistrictRequest districtRequest);

    /**
     * Elimina un distrito por su ID.
     * @param id identificador del distrito a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    void deleteDistrict(Integer id);
}
