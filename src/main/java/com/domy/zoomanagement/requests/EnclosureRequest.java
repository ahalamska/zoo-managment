package com.domy.zoomanagement.requests;

import com.domy.zoomanagement.models.Habitat;

import javax.persistence.*;

public class EnclosureRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habitat_id")
    private Habitat habitat;

    @Column(nullable = false)
    private String localization;

    @Column(nullable = false)
    private Float surface;

    @Column(nullable = false)
    private Float price;

    @Column
    private Boolean bought = false;
}
