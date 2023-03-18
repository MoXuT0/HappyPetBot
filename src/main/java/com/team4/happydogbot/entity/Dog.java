package com.team4.happydogbot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false, length = 25)
    private String name;
    @Column(name = "breed", nullable = false, length = 25)
    private String breed;
    @Column(name = "year_birth")
    private int yearOfBirth;
    @Column(name = "description")
    private String description;
    @OneToOne(mappedBy = "dog")
    @JsonBackReference
    private AdopterDog adopterDog;
}
