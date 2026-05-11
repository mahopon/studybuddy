package com.tcyao.studybuddy.identity.entities;

import com.tcyao.studybuddy.shared.entities.Timestamps;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User extends Timestamps {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = true)
    private String displayName;
    @Column(nullable = true)
    private int age;

    public User(String displayName, int age) {
        this.displayName = displayName;
        this.age = age;
    }
}
