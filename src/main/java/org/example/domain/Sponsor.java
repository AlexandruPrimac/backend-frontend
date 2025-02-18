package org.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "sponsors")
public class Sponsor {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String industry;

    @Column(nullable = false)
    private int foundingYear;

    @Column(nullable = false)
    private String image;

    // Many-to-Many with Car
    @OneToMany(mappedBy = "sponsor", fetch = FetchType.LAZY)
    private List<CarSponsors> cars = new ArrayList<>();
    ;

    // Constructor
    public Sponsor(int id, String name, String industry, int foundingYear, String image) {
        this.id = id;
        this.name = name;
        this.industry = industry;
        this.foundingYear = foundingYear;
        this.cars = new ArrayList<>();
        this.image = image;
    }

    public Sponsor(String name, String industry, int foundingYear, String image) {
        this.name = name;
        this.industry = industry;
        this.foundingYear = foundingYear;
        this.image = image;
    }

    public Sponsor() {
    }


    @Override
    public String toString() {
        return "The sponsor has the id " + id +
                ", it is named '" + name + '\'' +
                ", and it belongs to the '" + industry + '\'' + " industry. " +
                "The founding year is " + foundingYear + ".";
    }
}
