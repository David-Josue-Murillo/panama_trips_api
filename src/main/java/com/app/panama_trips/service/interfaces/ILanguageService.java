package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.LanguageRequest;
import com.app.panama_trips.presentation.dto.LanguageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Contrato de servicio para la gestión de idiomas disponibles para tours.
 */
public interface ILanguageService {

    /**
     * Obtiene todos los idiomas con paginación.
     * @param pageable configuración de paginación
     * @return página de idiomas
     */
    Page<LanguageResponse> getAllLanguages(Pageable pageable);

    /**
     * Obtiene un idioma por su código (ej. "es", "en").
     * @param code código del idioma
     * @return el idioma encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    LanguageResponse getLanguageByCode(String code);

    /**
     * Crea un nuevo idioma.
     * @param request datos del idioma a crear
     * @return el idioma creado
     */
    LanguageResponse saveLanguage(LanguageRequest request);

    /**
     * Actualiza un idioma existente.
     * @param code código del idioma a actualizar
     * @param request nuevos datos del idioma
     * @return el idioma actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    LanguageResponse updateLanguage(String code, LanguageRequest request);

    /**
     * Elimina un idioma por su código.
     * @param code código del idioma a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    void deleteLanguage(String code);

    /**
     * Obtiene todos los idiomas activos.
     * @return lista de idiomas activos
     */
    List<LanguageResponse> getAllActiveLanguages();

    /**
     * Obtiene un idioma por su nombre.
     * @param name nombre del idioma (ej. "Español")
     * @return el idioma encontrado
     */
    LanguageResponse getLanguageByName(String name);

    /**
     * Busca idiomas activos que coincidan con la palabra clave.
     * @param keyword término de búsqueda
     * @return lista de idiomas activos coincidentes
     */
    List<LanguageResponse> searchActiveLanguages(String keyword);

    /**
     * Cuenta el total de idiomas activos.
     * @return cantidad de idiomas activos
     */
    long countActiveLanguages();

    /**
     * Verifica si existe un idioma con el código indicado.
     * @param code código a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByCode(String code);

    /**
     * Verifica si existe un idioma con el nombre indicado.
     * @param name nombre a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
}
