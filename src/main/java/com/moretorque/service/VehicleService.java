package com.moretorque.service;

import com.moretorque.model.Vehicle;
import com.moretorque.repository.VehicleRepository;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final NhtsaService nhtsaService;

    public VehicleService(VehicleRepository vehicleRepository, NhtsaService nhtsaService) {
        this.vehicleRepository = vehicleRepository;
        this.nhtsaService = nhtsaService;
    }

    public String decodeVin(String vin) {
        return nhtsaService.decodeVin(vin);
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle getVehicle(String vin) {
        return vehicleRepository.findById(vin).orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }
}
