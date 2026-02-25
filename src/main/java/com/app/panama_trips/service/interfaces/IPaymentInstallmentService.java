package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.PaymentInstallmentRequest;
import com.app.panama_trips.presentation.dto.PaymentInstallmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de servicio para la gestion de cuotas de pago.
 * Provee operaciones CRUD, consultas por reserva/pago/estado, calculos financieros,
 * gestion de estados, recordatorios, estadisticas y operaciones en lote.
 */
public interface IPaymentInstallmentService {

    // ==================== CRUD operations ====================

    /**
     * Obtiene todas las cuotas de pago de forma paginada.
     *
     * @param pageable parametros de paginacion
     * @return pagina de cuotas de pago
     */
    Page<PaymentInstallmentResponse> getAllPaymentInstallments(Pageable pageable);

    /**
     * Obtiene una cuota de pago por su identificador.
     *
     * @param id identificador de la cuota
     * @return la cuota encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    PaymentInstallmentResponse getPaymentInstallmentById(Integer id);

    /**
     * Guarda una nueva cuota de pago.
     *
     * @param request datos de la cuota a guardar
     * @return la cuota persistida
     */
    PaymentInstallmentResponse savePaymentInstallment(PaymentInstallmentRequest request);

    /**
     * Actualiza una cuota de pago existente.
     *
     * @param id identificador de la cuota a actualizar
     * @param request datos actualizados de la cuota
     * @return la cuota actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    PaymentInstallmentResponse updatePaymentInstallment(Integer id, PaymentInstallmentRequest request);

    /**
     * Elimina una cuota de pago por su identificador.
     *
     * @param id identificador de la cuota a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    void deletePaymentInstallment(Integer id);

    // ==================== Find operations by entity relationships ====================

    /**
     * Busca cuotas de pago por identificador de reserva.
     *
     * @param reservationId identificador de la reserva
     * @return lista de cuotas asociadas a la reserva
     */
    List<PaymentInstallmentResponse> findByReservationId(Integer reservationId);

    /**
     * Busca cuotas de pago por identificador de pago.
     *
     * @param paymentId identificador del pago
     * @return lista de cuotas asociadas al pago
     */
    List<PaymentInstallmentResponse> findByPaymentId(Integer paymentId);

    /**
     * Busca cuotas de pago por estado.
     *
     * @param status estado de la cuota
     * @return lista de cuotas con el estado indicado
     */
    List<PaymentInstallmentResponse> findByStatus(String status);

    /**
     * Busca cuotas de pago con fecha de vencimiento anterior a la fecha dada.
     *
     * @param date fecha limite
     * @return lista de cuotas vencidas antes de la fecha
     */
    List<PaymentInstallmentResponse> findByDueDateBefore(LocalDate date);

    /**
     * Busca cuotas de pago con fecha de vencimiento dentro de un rango.
     *
     * @param startDate fecha de inicio del rango
     * @param endDate fecha de fin del rango
     * @return lista de cuotas dentro del rango
     */
    List<PaymentInstallmentResponse> findByDueDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Busca cuotas de pago segun si se envio recordatorio.
     *
     * @param reminderSent indicador de recordatorio enviado
     * @return lista de cuotas que coinciden
     */
    List<PaymentInstallmentResponse> findByReminderSent(Boolean reminderSent);

    // ==================== Specialized queries from repository ====================

    /**
     * Busca cuotas por reserva y estado.
     *
     * @param reservationId identificador de la reserva
     * @param status estado de la cuota
     * @return lista de cuotas que coinciden
     */
    List<PaymentInstallmentResponse> findByReservationIdAndStatus(Integer reservationId, String status);

    /**
     * Busca cuotas pendientes sin recordatorio enviado antes de una fecha.
     *
     * @param date fecha limite de vencimiento
     * @return lista de cuotas pendientes sin recordatorio
     */
    List<PaymentInstallmentResponse> findPendingInstallmentsWithoutReminder(LocalDate date);

    /**
     * Suma el monto pendiente de todas las cuotas de una reserva.
     *
     * @param reservationId identificador de la reserva
     * @return monto total pendiente
     */
    BigDecimal sumPendingAmountByReservation(Integer reservationId);

    /**
     * Cuenta las cuotas vencidas por estado y fecha.
     *
     * @param status estado de la cuota
     * @param date fecha limite de vencimiento
     * @return cantidad de cuotas vencidas
     */
    Long countOverdueInstallments(String status, LocalDate date);

    // ==================== Business logic operations ====================

    /**
     * Obtiene todas las cuotas vencidas.
     *
     * @return lista de cuotas vencidas
     */
    List<PaymentInstallmentResponse> getOverdueInstallments();

    /**
     * Obtiene todas las cuotas pendientes de pago.
     *
     * @return lista de cuotas pendientes
     */
    List<PaymentInstallmentResponse> getPendingInstallments();

    /**
     * Obtiene todas las cuotas pagadas.
     *
     * @return lista de cuotas pagadas
     */
    List<PaymentInstallmentResponse> getPaidInstallments();

    /**
     * Obtiene todas las cuotas canceladas.
     *
     * @return lista de cuotas canceladas
     */
    List<PaymentInstallmentResponse> getCancelledInstallments();

    /**
     * Obtiene las cuotas que requieren envio de recordatorio.
     *
     * @return lista de cuotas que necesitan recordatorio
     */
    List<PaymentInstallmentResponse> getInstallmentsRequiringReminder();

    /**
     * Obtiene cuotas de pago dentro de un rango de fechas de vencimiento.
     *
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de cuotas dentro del rango
     */
    List<PaymentInstallmentResponse> getInstallmentsByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Obtiene cuotas de una reserva filtradas por estado.
     *
     * @param reservationId identificador de la reserva
     * @param status estado de la cuota
     * @return lista de cuotas que coinciden
     */
    List<PaymentInstallmentResponse> getInstallmentsByReservationAndStatus(Integer reservationId, String status);

    // ==================== Advanced queries ====================

    /**
     * Obtiene las cuotas mas recientes limitadas por cantidad.
     *
     * @param limit cantidad maxima de resultados
     * @return lista de las cuotas mas recientes
     */
    List<PaymentInstallmentResponse> getRecentInstallments(int limit);

    /**
     * Obtiene cuotas dentro de un rango de montos.
     *
     * @param minAmount monto minimo
     * @param maxAmount monto maximo
     * @return lista de cuotas dentro del rango de montos
     */
    List<PaymentInstallmentResponse> getInstallmentsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);

    /**
     * Obtiene cuotas de pago asociadas a un usuario.
     *
     * @param userId identificador del usuario
     * @return lista de cuotas del usuario
     */
    List<PaymentInstallmentResponse> getInstallmentsByUser(Long userId);

    /**
     * Obtiene cuotas de pago asociadas a un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return lista de cuotas del plan de tour
     */
    List<PaymentInstallmentResponse> getInstallmentsByTourPlan(Integer tourPlanId);

    // ==================== Bulk operations ====================

    /**
     * Crea multiples cuotas de pago en lote.
     *
     * @param requests lista de solicitudes de cuotas a crear
     */
    void bulkCreatePaymentInstallments(List<PaymentInstallmentRequest> requests);

    /**
     * Elimina multiples cuotas de pago por sus identificadores.
     *
     * @param installmentIds lista de identificadores a eliminar
     */
    void bulkDeletePaymentInstallments(List<Integer> installmentIds);

    /**
     * Actualiza el estado de multiples cuotas de pago.
     *
     * @param installmentIds lista de identificadores de cuotas
     * @param newStatus nuevo estado a asignar
     */
    void bulkUpdateStatus(List<Integer> installmentIds, String newStatus);

    /**
     * Marca multiples cuotas como recordatorio enviado.
     *
     * @param installmentIds lista de identificadores de cuotas
     */
    void bulkMarkAsReminderSent(List<Integer> installmentIds);

    // ==================== Check operations ====================

    /**
     * Verifica si existe una cuota de pago por su identificador.
     *
     * @param id identificador de la cuota
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsById(Integer id);

    /**
     * Verifica si existen cuotas para una reserva.
     *
     * @param reservationId identificador de la reserva
     * @return {@code true} si existen cuotas, {@code false} en caso contrario
     */
    boolean existsByReservationId(Integer reservationId);

    /**
     * Verifica si existen cuotas para un pago.
     *
     * @param paymentId identificador del pago
     * @return {@code true} si existen cuotas, {@code false} en caso contrario
     */
    boolean existsByPaymentId(Integer paymentId);

    /**
     * Cuenta las cuotas de una reserva.
     *
     * @param reservationId identificador de la reserva
     * @return cantidad de cuotas
     */
    long countByReservationId(Integer reservationId);

    /**
     * Cuenta las cuotas por estado.
     *
     * @param status estado de la cuota
     * @return cantidad de cuotas
     */
    long countByStatus(String status);

    /**
     * Cuenta las cuotas con fecha de vencimiento anterior a la fecha dada.
     *
     * @param date fecha limite
     * @return cantidad de cuotas
     */
    long countByDueDateBefore(LocalDate date);

    /**
     * Cuenta las cuotas segun si se envio recordatorio.
     *
     * @param reminderSent indicador de recordatorio enviado
     * @return cantidad de cuotas
     */
    long countByReminderSent(Boolean reminderSent);

    // ==================== Financial operations ====================

    /**
     * Calcula el monto total de todas las cuotas de una reserva.
     *
     * @param reservationId identificador de la reserva
     * @return monto total
     */
    BigDecimal calculateTotalAmountForReservation(Integer reservationId);

    /**
     * Calcula el monto total pendiente de una reserva.
     *
     * @param reservationId identificador de la reserva
     * @return monto pendiente total
     */
    BigDecimal calculateTotalPendingAmountForReservation(Integer reservationId);

    /**
     * Calcula el monto total vencido de una reserva.
     *
     * @param reservationId identificador de la reserva
     * @return monto vencido total
     */
    BigDecimal calculateTotalOverdueAmountForReservation(Integer reservationId);

    /**
     * Calcula el total de recargos por mora de una reserva.
     *
     * @param reservationId identificador de la reserva
     * @return total de recargos por mora
     */
    BigDecimal calculateTotalLateFeesForReservation(Integer reservationId);

    /**
     * Calcula el monto total de cuotas dentro de un rango de fechas.
     *
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return monto total en el rango
     */
    BigDecimal calculateTotalAmountByDateRange(LocalDate startDate, LocalDate endDate);

    // ==================== Statistics and analytics ====================

    /**
     * Obtiene el total de cuotas en el sistema.
     *
     * @return cantidad total de cuotas
     */
    long getTotalInstallments();

    /**
     * Obtiene el total de cuotas pendientes.
     *
     * @return cantidad de cuotas pendientes
     */
    long getTotalPendingInstallments();

    /**
     * Obtiene el total de cuotas pagadas.
     *
     * @return cantidad de cuotas pagadas
     */
    long getTotalPaidInstallments();

    /**
     * Obtiene el total de cuotas vencidas.
     *
     * @return cantidad de cuotas vencidas
     */
    long getTotalOverdueInstallments();

    /**
     * Obtiene el total de cuotas canceladas.
     *
     * @return cantidad de cuotas canceladas
     */
    long getTotalCancelledInstallments();

    /**
     * Obtiene el monto total pendiente de pago.
     *
     * @return monto total pendiente
     */
    BigDecimal getTotalAmountPending();

    /**
     * Obtiene el monto total pagado.
     *
     * @return monto total pagado
     */
    BigDecimal getTotalAmountPaid();

    /**
     * Obtiene el monto total vencido.
     *
     * @return monto total vencido
     */
    BigDecimal getTotalAmountOverdue();

    /**
     * Obtiene el total de recargos por mora en el sistema.
     *
     * @return total de recargos por mora
     */
    BigDecimal getTotalLateFees();

    /**
     * Calcula la tasa de exito de pagos.
     *
     * @return porcentaje de exito de pagos (0.0 a 100.0)
     */
    double getPaymentSuccessRate();

    /**
     * Obtiene las reservas con mayor cantidad de cuotas.
     *
     * @param limit cantidad maxima de resultados
     * @return lista de cuotas de las reservas con mas cuotas
     */
    List<PaymentInstallmentResponse> getTopReservationsByInstallmentCount(int limit);

    /**
     * Obtiene las cuotas agrupadas por mes.
     *
     * @return lista de cuotas agrupadas por mes
     */
    List<PaymentInstallmentResponse> getInstallmentsByMonth();

    /**
     * Obtiene las cuotas agrupadas por dia de la semana.
     *
     * @return lista de cuotas agrupadas por dia de la semana
     */
    List<PaymentInstallmentResponse> getInstallmentsByDayOfWeek();

    // ==================== Status management operations ====================

    /**
     * Marca una cuota como pagada.
     *
     * @param installmentId identificador de la cuota
     * @return la cuota actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    PaymentInstallmentResponse markAsPaid(Integer installmentId);

    /**
     * Marca una cuota como vencida.
     *
     * @param installmentId identificador de la cuota
     * @return la cuota actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    PaymentInstallmentResponse markAsOverdue(Integer installmentId);

    /**
     * Marca una cuota como cancelada.
     *
     * @param installmentId identificador de la cuota
     * @return la cuota actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    PaymentInstallmentResponse markAsCancelled(Integer installmentId);

    /**
     * Marca una cuota como pendiente.
     *
     * @param installmentId identificador de la cuota
     * @return la cuota actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    PaymentInstallmentResponse markAsPending(Integer installmentId);

    /**
     * Verifica si una transicion de estado es valida para una cuota.
     *
     * @param installmentId identificador de la cuota
     * @param newStatus nuevo estado propuesto
     * @return {@code true} si la transicion es valida, {@code false} en caso contrario
     */
    boolean isValidStatusTransition(Integer installmentId, String newStatus);

    /**
     * Obtiene las transiciones de estado validas para una cuota.
     *
     * @param installmentId identificador de la cuota
     * @return lista de estados validos para transicionar
     */
    List<String> getValidStatusTransitions(Integer installmentId);

    // ==================== Reminder operations ====================

    /**
     * Marca el recordatorio de una cuota como enviado.
     *
     * @param installmentId identificador de la cuota
     * @return la cuota actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    PaymentInstallmentResponse markReminderAsSent(Integer installmentId);

    /**
     * Obtiene las cuotas que necesitan recordatorio.
     *
     * @return lista de cuotas que requieren recordatorio
     */
    List<PaymentInstallmentResponse> getInstallmentsNeedingReminder();

    /**
     * Envia recordatorios para las cuotas proximas a vencer.
     */
    void sendRemindersForDueInstallments();

    /**
     * Envia recordatorios para las cuotas vencidas.
     */
    void sendRemindersForOverdueInstallments();

    // ==================== Utility operations ====================

    /**
     * Recalcula el estado de vencimiento de todas las cuotas.
     */
    void recalculateOverdueStatus();

    /**
     * Elimina cuotas antiguas segun la cantidad de dias a conservar.
     *
     * @param daysToKeep cantidad de dias a mantener
     */
    void cleanupOldInstallments(int daysToKeep);

    /**
     * Busca cuotas por monto exacto.
     *
     * @param amount monto a buscar
     * @return lista de cuotas con el monto indicado
     */
    List<PaymentInstallmentResponse> searchInstallmentsByAmount(BigDecimal amount);

    /**
     * Busca la cuota mas reciente de una reserva.
     *
     * @param reservationId identificador de la reserva
     * @return la cuota mas reciente, o vacio si no existe
     */
    Optional<PaymentInstallmentResponse> findLatestInstallmentByReservation(Integer reservationId);

    /**
     * Obtiene todas las cuotas que tienen recargos por mora.
     *
     * @return lista de cuotas con recargos
     */
    List<PaymentInstallmentResponse> getInstallmentsWithLateFees();

    /**
     * Calcula el recargo por mora de una cuota especifica.
     *
     * @param installmentId identificador de la cuota
     * @return monto del recargo por mora
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    BigDecimal calculateLateFeeForInstallment(Integer installmentId);
}
