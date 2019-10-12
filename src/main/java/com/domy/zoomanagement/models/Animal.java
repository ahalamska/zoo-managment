package com.domy.zoomanagement.models;


import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "species", nullable = false)
    private String species;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;
}