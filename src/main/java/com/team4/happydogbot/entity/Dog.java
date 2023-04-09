package com.team4.happydogbot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс, описывающий обитателя собачьего приюта
 * @see Dog
 * @see AdopterDog
 */
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

    public Dog(Long id, String name, String breed, int yearOfBirth, String description) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.yearOfBirth = yearOfBirth;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog dog = (Dog) o;
        return yearOfBirth == dog.yearOfBirth && Objects.equals(id, dog.id) && Objects.equals(name, dog.name) && Objects.equals(breed, dog.breed) && Objects.equals(description, dog.description) && Objects.equals(adopterDog, dog.adopterDog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, breed, yearOfBirth, description, adopterDog);
    }
}
