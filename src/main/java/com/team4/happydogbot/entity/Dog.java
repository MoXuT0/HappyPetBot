package com.team4.happydogbot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dog")
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_id")
    private Long id;
    @Column(name = "breed")
    private String breed;
    @Column(name = "name")
    private String name;
    @Column(name = "year_birth")
    private int yearOfBirth;
    @Column(name = "description")
    private String description;
//    @OneToOne(mappedBy = "dog", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonManagedReference
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    @JsonBackReference
    private AdopterDog adopterDog;

    public Dog(Long id, String breed, String name, int yearOfBirth, String description) {
        this.id = id;
        this.breed = breed;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.description = description;
    }
}
