package org.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "car_races", uniqueConstraints = {@UniqueConstraint(name = "u_car_races", columnNames = {"car_id", "race_id"})})
public class CarRaces {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id", nullable = false)
    private Car car;

    @ManyToOne
    @JoinColumn(name = "race_id", referencedColumnName = "id", nullable = false)
    private Race race;


}
