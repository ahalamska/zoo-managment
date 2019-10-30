package com.domy.zoomanagement.requests;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class SpeciesRequest {
    @NotNull
    private String name;

    private String naturalHabitat;

    private String description;

    private String photoUrl;

    @NotNull
    private String food;

    @NotNull
    private Float price;
}
