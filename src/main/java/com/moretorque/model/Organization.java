package com.moretorque.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "organizations")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String account;
    private String website;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Organization parent;

    @OneToMany(mappedBy = "parent")
    private Set<Organization> children;

    @OneToMany(mappedBy = "organization")
    private Set<Vehicle> vehicles;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private Set<Policy> policies;

}

