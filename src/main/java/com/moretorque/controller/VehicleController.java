package com.moretorque.controller;

import com.moretorque.model.Vehicle;
import com.moretorque.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/decode/{vin}")
    public ResponseEntity<String> decodeVin(@PathVariable String vin) {
        return ResponseEntity.ok(vehicleService.decodeVin(vin));
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.createVehicle(vehicle));
    }

    @GetMapping("/{vin}")
    public ResponseEntity<Vehicle> getVehicle(@PathVariable String vin) {
        return ResponseEntity.ok(vehicleService.getVehicle(vin));
    }
}