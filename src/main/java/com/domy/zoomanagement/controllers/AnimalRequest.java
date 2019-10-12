package com.domy.zoomanagement.controllers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimalRequest {
    private String species;
    private String name;
    private Long room;
}
