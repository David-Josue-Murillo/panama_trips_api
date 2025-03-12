package com.app.panama_trips.exception;

import java.util.Map;

public record ErrorResponse(
        int status,
        String message,
        long timestamp,
        String errorCode,
        Map<String, String> details
) {
    public ErrorResponse(int status, String message, long timestamp) {
        this(status, message, timestamp, null, null);
    }
}
