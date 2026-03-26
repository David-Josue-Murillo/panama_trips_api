package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.ComarcaRequest;
import com.app.panama_trips.presentation.dto.ComarcaResponse;

import java.util.List;

/**
 * Contrato de servicio para la gestión de comarcas de Panamá.
 */
public interface IComarcaService {

  /**
   * Obtiene todas las comarcas.
   * 
   * @return lista de comarcas
   */
  List<ComarcaResponse> getAllComarcas();

  /**
   * Obtiene una comarca por su ID.
   * 
   * @param id identificador de la comarca
   * @return la comarca encontrada
   */
  ComarcaResponse getComarcaById(Integer id);

  /**
   * Obtiene una comarca por su nombre.
   * 
   * @param name nombre de la comarca
   * @return la comarca encontrada
   */
  ComarcaResponse getComarcaByName(String name);

  /**
   * Crea una nueva comarca.
   * 
   * @param comarcaRequest datos de la comarca a crear
   * @return la comarca creada
   */
  ComarcaResponse saveComarca(ComarcaRequest comarcaRequest);

  /**
   * Actualiza una comarca existente.
   * 
   * @param id             identificador de la comarca a actualizar
   * @param comarcaRequest nuevos datos de la comarca
   * @return la comarca actualizada
   */
  ComarcaResponse updateComarca(Integer id, ComarcaRequest comarcaRequest);

  /**
   * Elimina una comarca por su ID.
   * 
   * @param id identificador de la comarca a eliminar
   */
  void deleteComarca(Integer id);
}
