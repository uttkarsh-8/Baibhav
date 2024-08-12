package com.moretorque.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Invalid VIN format")
    private String vin;

    @NotNull(message = "Make cannot be null")
    @Column(nullable = false)
    private String make;

    @NotNull(message = "Model cannot be null")
    @Column(nullable = false)
    private String model;

    @NotNull(message = "Year cannot be null")
    @Column(nullable = false)
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

}
