package com.domy.zoomanagement.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinColumn(name = "caretaker_id")
    private Set<Caretaker> caretakers;

//    @OneToMany(mappedBy = "room")
//    private List<Animal> animals;
}
