package com.domy.zoomanagement.requests;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class AnimalRequest {
    @NotNull
    private String species;
    private String name;
    @NotNull
    private Long room;
}
