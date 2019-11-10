package com.domy.zoomanagement.requests;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class RoomResponse {

    private Long id;

    private boolean bought = false;

    private int locatorsMaxNumber;


    private List<String> species;

    private Float surface;

    private String localization;


    private Long enclosureId;

    private Long caretakerId;

    private Float price;
}
