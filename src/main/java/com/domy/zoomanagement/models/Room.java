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
    private boolean bought = false;

    @Column
    private int locatorsMaxNumber;

    @OneToMany
    @Column(nullable = false)
    private List<Species> species;

    @Column(nullable = false)
    private Float surface;

    @Column(nullable = false)
    private String localization;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "enclosure_id")
    private Enclosure enclosure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caretaker_id")
    private Caretaker caretaker;

    @Column(nullable = false)
    private Float price;



}
