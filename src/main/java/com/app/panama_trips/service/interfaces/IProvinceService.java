package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.ProvinceRequest;
import com.app.panama_trips.presentation.dto.ProvinceResponse;

import java.util.List;

/**
 * Contrato de servicio para la gestión de provincias de Panamá.
 */
public interface IProvinceService {

    /**
     * Obtiene todas las provincias.
     * @return lista de provincias
     */
    List<ProvinceResponse> getAllProvinces();

    /**
     * Obtiene una provincia por su ID.
     * @param id identificador de la provincia
     * @return la provincia encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    ProvinceResponse getProvinceById(Integer id);

    /**
     * Obtiene una provincia por su nombre.
     * @param name nombre de la provincia
     * @return la provincia encontrada
     */
    ProvinceResponse getProvinceByName(String name);

    /**
     * Crea una nueva provincia.
     * @param provinceRequest datos de la provincia a crear
     * @return la provincia creada
     */
    ProvinceResponse saveProvince(ProvinceRequest provinceRequest);

    /**
     * Actualiza una provincia existente.
     * @param id identificador de la provincia a actualizar
     * @param provinceRequest nuevos datos de la provincia
     * @return la provincia actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    ProvinceResponse updateProvince(Integer id, ProvinceRequest provinceRequest);

    /**
     * Elimina una provincia por su ID.
     * @param id identificador de la provincia a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    void deleteProvince(Integer id);
}
