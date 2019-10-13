package com.domy.zoomanagement.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "species")
public class Species {

    @Id
    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String naturalHabitat;

    @Column
    private String description;

    @Column(nullable = false)
    private String food;

    @Column(nullable = false)
    private Float price;
}
