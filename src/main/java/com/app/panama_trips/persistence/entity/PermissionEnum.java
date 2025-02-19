package com.app.panama_trips.persistence.entity;

public enum PermissionEnum {
    // Permisos para gestión de usuarios
    USER_READ,
    USER_CREATE,
    USER_UPDATE,
    USER_DELETE,

    // Permisos para gestión de reservas
    BOOKING_READ,
    BOOKING_CREATE,
    BOOKING_UPDATE,
    BOOKING_DELETE,

    // Permisos para gestión de pagos
    PAYMENT_READ,
    PAYMENT_REFUND,

    // Permisos para soporte técnico
    SUPPORT_TICKET_READ,
    SUPPORT_TICKET_RESPOND,

    // Permisos para la gestión de contenido
    CONTENT_CREATE,
    CONTENT_UPDATE,
    CONTENT_DELETE,

    // Permisos para ver logs y monitoreo del sistema
    SYSTEM_LOG_READ
}
