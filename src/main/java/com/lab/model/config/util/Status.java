package com.lab.model.config.util;

import java.util.Arrays;

public enum Status {
    APPROVED,
    REJECTED,
    PENDING;
    public static Status fromString(String status) {
        return Arrays.stream(Status.values())
                .filter(s -> s.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown status: " + status));
    }
}
