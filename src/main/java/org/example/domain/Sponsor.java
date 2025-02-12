package org.example.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sponsors")
public class Sponsor {

    /// Attributes
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

    /// Constructor
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

    //Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getFoundingYear() {
        return foundingYear;
    }

    public void setFoundingYear(int foundingYear) {
        this.foundingYear = foundingYear;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<CarSponsors> getCars() {
        return cars;
    }

    public void setCars(List<CarSponsors> cars) {
        this.cars = cars;
    }


    @Override
    public String toString() {
        return "The sponsor has the id " + id +
                ", it is named '" + name + '\'' +
                ", and it belongs to the '" + industry + '\'' + " industry. " +
                "The founding year is " + foundingYear + ".";
    }
}
