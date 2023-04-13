package com.team4.happydogbot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс, описывающий обитателя кошачьего приюта
 * @see Cat
 * @see AdopterCat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cat")
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private Long id;
    @Column(name = "name", nullable = false, length = 25)
    private String name;
    @Column(name = "breed", nullable = false, length = 25)
    private String breed;
    @Column(name = "year_birth")
    private int yearOfBirth;
    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "cat", fetch = FetchType.EAGER)
    @JsonBackReference
    private AdopterCat adopterCat;

    public Cat(Long id, String name, String breed, int yearOfBirth, String description) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.yearOfBirth = yearOfBirth;
        this.description = description;
    }
}
