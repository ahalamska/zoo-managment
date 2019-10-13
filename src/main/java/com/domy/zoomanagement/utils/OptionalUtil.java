package com.domy.zoomanagement.utils;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.Optional;

public class OptionalUtil {
    public static <T> T handleNullable(T nullable) {
        return Optional.of(nullable).orElseThrow(() -> new ResourceNotFoundException("Object is null"));
    }
}
