package com.app.panama_trips.persistence.entity.enums;

import java.util.List;

public enum AuditAction {
    // Authentication
    LOGIN_SUCCESS("LOGIN_SUCCESS", "Successful login"),
    LOGIN_FAILED("LOGIN_FAILED", "Failed login attempt"),
    AUTHENTICATION_FAILED("AUTHENTICATION_FAILED", "Authentication failure"),
    FAILED_LOGIN("FAILED_LOGIN", "Failed login"),
    AUTHENTICATION_SUCCESS("AUTHENTICATION_SUCCESS", "Successful authentication"),
    SUCCESSFUL_LOGIN("SUCCESSFUL_LOGIN", "Successful login"),

    // CRUD
    CREATE("CREATE", "Resource created"),
    READ("READ", "Resource read"),
    UPDATE("UPDATE", "Resource updated"),
    DELETE("DELETE", "Resource deleted"),

    // Aliases
    VIEW("VIEW", "Resource viewed"),
    ACCESS("ACCESS", "Resource accessed"),
    MODIFY("MODIFY", "Resource modified"),
    EDIT("EDIT", "Resource edited"),
    REMOVE("REMOVE", "Resource removed"),
    DESTROY("DESTROY", "Resource destroyed"),

    // Status
    FAILED("FAILED", "Operation failed"),
    ERROR("ERROR", "Error occurred");

    private final String code;
    private final String description;

    private static final List<String> FAILED_LOGIN_ACTIONS = List.of(
            LOGIN_FAILED.code, AUTHENTICATION_FAILED.code, FAILED_LOGIN.code
    );

    private static final List<String> SUCCESSFUL_LOGIN_ACTIONS = List.of(
            LOGIN_SUCCESS.code, AUTHENTICATION_SUCCESS.code, SUCCESSFUL_LOGIN.code
    );

    private static final List<String> DATA_MODIFICATION_ACTIONS = List.of(
            CREATE.code, UPDATE.code, DELETE.code, MODIFY.code, EDIT.code, REMOVE.code, DESTROY.code
    );

    private static final List<String> DATA_ACCESS_ACTIONS = List.of(
            READ.code, VIEW.code, ACCESS.code
    );

    private static final List<String> DATA_DELETION_ACTIONS = List.of(
            DELETE.code, REMOVE.code, DESTROY.code
    );

    private static final List<String> SUSPICIOUS_ACTIONS = List.of(
            FAILED.code, ERROR.code, LOGIN_FAILED.code, AUTHENTICATION_FAILED.code, FAILED_LOGIN.code
    );

    AuditAction(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static List<String> getFailedLoginActions() {
        return FAILED_LOGIN_ACTIONS;
    }

    public static List<String> getSuccessfulLoginActions() {
        return SUCCESSFUL_LOGIN_ACTIONS;
    }

    public static List<String> getDataModificationActions() {
        return DATA_MODIFICATION_ACTIONS;
    }

    public static List<String> getDataAccessActions() {
        return DATA_ACCESS_ACTIONS;
    }

    public static List<String> getDataDeletionActions() {
        return DATA_DELETION_ACTIONS;
    }

    public static List<String> getSuspiciousActions() {
        return SUSPICIOUS_ACTIONS;
    }

    public static AuditAction fromString(String action) {
        for (AuditAction auditAction : values()) {
            if (auditAction.code.equalsIgnoreCase(action)) {
                return auditAction;
            }
        }
        throw new IllegalArgumentException("Unknown audit action: " + action);
    }
}
