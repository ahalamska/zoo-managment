package com.domy.zoomanagement.requests;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class EntertainerRequest {
    @NotNull
    private String firstName;

    @NotNull
    private String name;
}
