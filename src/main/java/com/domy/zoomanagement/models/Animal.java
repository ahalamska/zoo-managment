package com.domy.zoomanagement.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CrossOrigin
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String species;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "room_id")
//    private Room room;
}
