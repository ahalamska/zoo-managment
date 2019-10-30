package com.domy.zoomanagement.requests;

import com.domy.zoomanagement.models.Contract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaretakerRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String name;

    @NotNull
    private Integer roomMaxNumber;

    @NotNull
    private Long contractId;
}
