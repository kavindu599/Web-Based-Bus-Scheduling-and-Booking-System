package com.company.bus_mgmt.exception;

import java.util.List;

public record ApiError(String type, String title, int status, String detail, String instance,
                       List<FieldError> errors) {
    public record FieldError(String field, String message) {}
    public static ApiError of(int status, String title, String detail) {
        return new ApiError("about:blank", title, status, detail, null, null);
    }
}
