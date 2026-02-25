package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.Provider;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.presentation.dto.GuideRequest;
import com.app.panama_trips.presentation.dto.GuideResponse;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contrato de servicio para la gestion de guias turisticos.
 * Provee operaciones CRUD, busquedas especializadas y gestion del estado activo/inactivo de guias.
 */
public interface IGuideService {

    // CRUD operations

    /**
     * Obtiene todos los guias de forma paginada.
     *
     * @param pageable configuracion de paginacion
     * @return pagina de guias
     */
    Page<GuideResponse> findAll(Pageable pageable);

    /**
     * Crea un nuevo guia turistico.
     *
     * @param guideRequest datos del guia a crear
     * @return el guia creado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el usuario o proveedor no existe
     */
    GuideResponse createGuide(GuideRequest guideRequest);

    /**
     * Actualiza un guia existente.
     *
     * @param id identificador del guia
     * @param guideRequest datos actualizados del guia
     * @return el guia actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el guia no existe
     */
    GuideResponse updateGuide(Integer id, GuideRequest guideRequest);

    /**
     * Elimina un guia por su identificador.
     *
     * @param id identificador del guia
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el guia no existe
     */
    void deleteGuide(Integer id);

    /**
     * Busca un guia por su identificador.
     *
     * @param id identificador del guia
     * @return el guia encontrado o vacio si no existe
     */
    Optional<GuideResponse> findById(Integer id);

    // Repository-based specific methods

    /**
     * Obtiene todos los guias activos.
     *
     * @return lista de guias activos
     */
    List<GuideResponse> findAllActive();

    /**
     * Obtiene los guias asociados a un proveedor.
     *
     * @param provider entidad del proveedor
     * @return lista de guias del proveedor
     */
    List<GuideResponse> findByProvider(Provider provider);

    /**
     * Busca un guia por su usuario asociado.
     *
     * @param user entidad del usuario
     * @return el guia encontrado o vacio si no existe
     */
    Optional<GuideResponse> findByUser(UserEntity user);

    /**
     * Obtiene guias con experiencia mayor o igual a los anios indicados.
     *
     * @param years anios minimos de experiencia
     * @return lista de guias que cumplen el criterio
     */
    List<GuideResponse> findByYearsExperienceGreaterThanEqual(Integer years);

    /**
     * Obtiene guias activos de un proveedor especifico.
     *
     * @param providerId identificador del proveedor
     * @return lista de guias activos del proveedor
     */
    List<GuideResponse> findActiveGuidesByProvider(Integer providerId);

    /**
     * Obtiene guias activos que hablan un idioma especifico.
     *
     * @param language idioma a buscar
     * @return lista de guias activos con el idioma indicado
     */
    List<GuideResponse> findByLanguageAndActive(String language);

    /**
     * Obtiene guias activos con una especialidad especifica.
     *
     * @param specialty especialidad a buscar
     * @return lista de guias activos con la especialidad indicada
     */
    List<GuideResponse> findBySpecialtyAndActive(String specialty);

    // Additional useful methods

    /**
     * Activa un guia por su identificador.
     *
     * @param id identificador del guia
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el guia no existe
     */
    void activateGuide(Integer id);

    /**
     * Desactiva un guia por su identificador.
     *
     * @param id identificador del guia
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el guia no existe
     */
    void deactivateGuide(Integer id);

    /**
     * Verifica si existe un guia con el identificador dado.
     *
     * @param id identificador del guia
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsById(Integer id);

    /**
     * Verifica si existe un guia asociado al usuario dado.
     *
     * @param user entidad del usuario
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsByUser(UserEntity user);
}
