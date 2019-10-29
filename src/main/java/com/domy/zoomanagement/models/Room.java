package com.domy.zoomanagement.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private int locatorsMaxNumber;

    @OneToMany
    @JoinColumn(name = "species_name")
    private List<Species> species;

    @Column(nullable = false)
    private Float surface;

    /*@Column(nullable = false)
    private Localization localization;*/

    /*@Column(nullable = false)
    private Enclosure enclosure;

    @Column(nullable = false)
    private Caretaker caretaker;*/

    @Column(nullable = false)
    private Float price;



}
