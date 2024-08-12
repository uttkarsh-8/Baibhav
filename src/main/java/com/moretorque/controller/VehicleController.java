package com.moretorque.controller;

import com.moretorque.model.Vehicle;
import com.moretorque.service.VehicleService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicles")
@Validated
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/decode/{vin}")
    public ResponseEntity<String> decodeVin(
            @PathVariable @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Invalid VIN format") String vin) {
        return ResponseEntity.ok(vehicleService.decodeVin(vin));
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.createVehicle(vehicle));
    }

    @GetMapping("/{vin}")
    public ResponseEntity<Vehicle> getVehicle(
            @PathVariable @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Invalid VIN format") String vin) {
        return ResponseEntity.ok(vehicleService.getVehicle(vin));
    }
}