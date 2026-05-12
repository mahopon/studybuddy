package com.tcyao.studybuddy.identity.entities;

import com.tcyao.studybuddy.shared.entities.Timestamps;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = true)
    private String displayName;
    @Column(nullable = true)
    private int age;

    private Timestamps timestamps;

    public User(String displayName, int age) {
        this.displayName = displayName;
        this.age = age;
    }
}
