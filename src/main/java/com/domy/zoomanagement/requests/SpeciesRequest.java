package com.domy.zoomanagement.requests;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class SpeciesRequest {
    @NonNull
    private String name;

    private String naturalHabitat;

    private String description;

    @NonNull
    private String food;

    @NonNull
    private Float price;
}
