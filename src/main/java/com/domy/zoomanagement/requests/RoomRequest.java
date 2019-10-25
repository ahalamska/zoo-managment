package com.domy.zoomanagement.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RoomRequest {
    @NotNull
    private String speciesName;
    @NotNull
    private Integer locatorsMaxNumber;
    @NotNull
    private Float surface;
    /*private Long localizationId;
    private Long enclosureId;
    private Long caretakerId;*/
    @NotNull
    private Float price;
}
