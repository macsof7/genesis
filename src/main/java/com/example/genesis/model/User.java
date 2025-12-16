package com.example.genesis.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "user_registry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String surname;
    @Column(name = "person_id", unique = true, nullable = false, length = 12)
    private String personId;
    @Column(name = "uuid", unique = true, nullable = false)
    private String uuid;
    public User(String name, String surname, String personId) {
        this.name = name;
        this.surname = surname;
        this.personId = personId;
    }
}