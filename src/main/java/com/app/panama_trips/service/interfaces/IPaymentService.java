package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.PaymentStatus;
import com.app.panama_trips.presentation.dto.PaymentRequest;
import com.app.panama_trips.presentation.dto.PaymentResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrato de servicio para la gestión de pagos de reservaciones.
 * Provee operaciones CRUD y consultas avanzadas para los pagos.
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
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la reservación asociada no existe
     */
    PaymentResponse savePayment(PaymentRequest request);

    /**
     * Actualiza un pago existente.
     *
     * @param id identificador del pago a actualizar
     * @param request datos actualizados del pago
     * @return el pago actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el pago o la reservación no existen
     */
    PaymentResponse updatePayment(Long id, PaymentRequest request);

    /**
     * Elimina un pago por su identificador.
     *
     * @param id identificador del pago a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el pago no existe
     */
    void deletePayment(Long id);

    // ─────────────────────────────────────────────
    //  Búsquedas / Filtros
    // ─────────────────────────────────────────────

    /**
     * Obtiene los pagos que tienen un estado determinado.
     *
     * @param status estado de pago a filtrar
     * @return lista de pagos con ese estado
     */
    List<PaymentResponse> getPaymentsByStatus(PaymentStatus status);

    /**
     * Obtiene los pagos asociados a un usuario específico.
     *
     * @param userId identificador del usuario
     * @return lista de pagos del usuario
     */
    List<PaymentResponse> getPaymentsByUser(Long userId);

    /**
     * Obtiene los pagos asociados a una reservación.
     *
     * @param reservationId identificador de la reservación
     * @return lista de pagos de la reservación
     */
    List<PaymentResponse> getPaymentsByReservation(Long reservationId);

    /**
     * Obtiene los pagos cuya fecha de creación cae dentro del rango indicado.
     *
     * @param startDate inicio del rango (inclusive)
     * @param endDate   fin del rango (inclusive)
     * @return lista de pagos en el rango de fechas
     */
    List<PaymentResponse> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    // ─────────────────────────────────────────────
    //  Estadísticas
    // ─────────────────────────────────────────────

    /**
     * Cuenta los pagos que tienen un estado determinado.
     *
     * @param status estado a contabilizar
     * @return número de pagos con ese estado
     */
    long countPaymentsByStatus(PaymentStatus status);

    // ─────────────────────────────────────────────
    //  Acciones de estado
    // ─────────────────────────────────────────────

    /**
     * Aprueba (marca como COMPLETED) un pago existente.
     *
     * @param id identificador del pago
     * @return el pago actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el pago no existe
     */
    PaymentResponse approvePayment(Long id);

    /**
     * Rechaza (marca como FAILED) un pago existente.
     *
     * @param id identificador del pago
     * @return el pago actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el pago no existe
     */
    PaymentResponse rejectPayment(Long id);
}

