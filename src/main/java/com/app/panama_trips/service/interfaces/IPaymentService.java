package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.PaymentRequest;
import com.app.panama_trips.presentation.dto.PaymentResponse;

import java.util.List;

/**
 * Contrato de servicio para la gestion de pagos de reservaciones.
 * Provee operaciones CRUD básicas para los pagos.
 */
public interface IPaymentService {

    /**
     * Obtiene todos los pagos registrados en el sistema.
     *
     * @return lista de pagos
     */
    List<PaymentResponse> getAllPayments();

    /**
     * Obtiene un pago por su identificador.
     *
     * @param id identificador del pago
     * @return el pago encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el pago no existe
     */
    PaymentResponse getPaymentById(Long id);

    /**
     * Registra un nuevo pago.
     *
     * @param request datos del pago a crear
     * @return el pago creado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la reservacion asociada no existe
     */
    PaymentResponse savePayment(PaymentRequest request);

    /**
     * Actualiza un pago existente.
     *
     * @param id identificador del pago a actualizar
     * @param request datos actualizados del pago
     * @return el pago actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el pago o la reservacion no existen
     */
    PaymentResponse updatePayment(Long id, PaymentRequest request);

    /**
     * Elimina un pago por su identificador.
     *
     * @param id identificador del pago a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el pago no existe
     */
    void deletePayment(Long id);
}
