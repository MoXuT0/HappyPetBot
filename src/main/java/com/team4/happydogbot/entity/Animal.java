package com.team4.happydogbot.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//@Entity
//@Data
public class Animal {

//    @Id
//    @GeneratedValue
    private Long id;

    private String breed;

    private String name;

    private int yearOfBirth;

    private String description;
}
