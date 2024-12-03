package com.anrideborze.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private String characterClass;
    @Setter
    @Getter
    private int level;
    @Setter
    @Getter
    private int strength;
    @Setter
    @Getter
    private int dexterity;
    @Setter
    @Getter
    private int constitution;

    // Getters and setters
}