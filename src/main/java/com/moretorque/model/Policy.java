package com.moretorque.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "policies")
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyType type;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    public enum PolicyType {
        FUEL_REIMBURSEMENT,
        SPEED_LIMIT
    }
}