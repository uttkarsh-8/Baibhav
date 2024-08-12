package com.moretorque.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
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

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    private String account;
    private String website;

    @Column(name = "fuel_reimbursement_policy")
    private String fuelReimbursementPolicy;

    @Column(name = "speed_limit_policy")
    private String speedLimitPolicy;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Organization parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Organization> children = new HashSet<>();

    @OneToMany(mappedBy = "organization")
    private Set<Vehicle> vehicles = new HashSet<>();

    // Getters and setters

    public String getEffectiveFuelReimbursementPolicy() {
        if (this.fuelReimbursementPolicy != null) {
            return this.fuelReimbursementPolicy;
        } else if (this.parent != null) {
            return this.parent.getEffectiveFuelReimbursementPolicy();
        }
        return null;
    }

    public String getEffectiveSpeedLimitPolicy() {
        return this.speedLimitPolicy != null ? this.speedLimitPolicy :
                (this.parent != null ? this.parent.getEffectiveSpeedLimitPolicy() : null);
    }

    public void addChild(Organization child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Organization child) {
        children.remove(child);
        child.setParent(null);
    }
}