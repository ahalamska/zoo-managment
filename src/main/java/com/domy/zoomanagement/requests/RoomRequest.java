package com.domy.zoomanagement.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoomRequest {

    @NotNull
    private List<String> speciesNames;

    @NotNull
    private Integer locatorsMaxNumber;

    @NotNull
    private Float surface;

    @NotNull
    private String localization;

    private Long enclosureId;

    private Long caretakerId;

    @NotNull
    private Float price;
}
