package com.domy.zoomanagement.requests;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AnimalRequest {
    private String species;
    private String name;
    private Long room;
}
