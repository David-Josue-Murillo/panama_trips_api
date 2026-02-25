package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.UserRequest;
import com.app.panama_trips.presentation.dto.UserResponse;
import org.springframework.data.domain.Page;

/**
 * Contrato de servicio para la gestión de usuarios del sistema.
 */
public interface IUserEntityService {

    /**
     * Obtiene todos los usuarios con paginación opcional.
     * @param page número de página (base 0)
     * @param size cantidad de elementos por página
     * @param enabledPagination true para activar paginación, false para obtener todos
     * @return página de usuarios
     */
    Page<UserResponse> getAllUser(Integer page, Integer size, Boolean enabledPagination);

    /**
     * Obtiene un usuario por su ID.
     * @param id identificador del usuario
     * @return el usuario encontrado
     * @throws com.app.panama_trips.exception.UserNotFoundException si no existe
     */
    UserResponse getUserById(Long id);

    /**
     * Crea un nuevo usuario con rol CUSTOMER por defecto.
     * @param userRequest datos del usuario a crear
     * @return el usuario creado
     * @throws com.app.panama_trips.exception.ValidationException si el email ya existe
     */
    UserResponse saveUser(UserRequest userRequest);

    /**
     * Actualiza un usuario existente.
     * @param id identificador del usuario a actualizar
     * @param userRequest nuevos datos del usuario
     * @return el usuario actualizado
     * @throws com.app.panama_trips.exception.UserNotFoundException si no existe
     */
    UserResponse updateUser(Long id, UserRequest userRequest);

    /**
     * Elimina un usuario por su ID.
     * @param id identificador del usuario a eliminar
     * @throws com.app.panama_trips.exception.UserNotFoundException si no existe
     */
    void deleteUser(Long id);
}
