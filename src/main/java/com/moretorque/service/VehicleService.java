package com.moretorque.service;

import com.moretorque.model.Vehicle;
import com.moretorque.repository.OrganizationRepository;
import com.moretorque.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final OrganizationRepository organizationRepository;
    private final NhtsaService nhtsaService;

    public VehicleService(VehicleRepository vehicleRepository, OrganizationRepository organizationRepository, NhtsaService nhtsaService) {
        this.vehicleRepository = vehicleRepository;
        this.organizationRepository = organizationRepository;
        this.nhtsaService = nhtsaService;
    }

    public String decodeVin(String vin) {
        return nhtsaService.decodeVin(vin);
    }

    @Transactional
    public Vehicle createVehicle(Vehicle vehicle) {
        if (vehicle.getOrganization() != null && vehicle.getOrganization().getId() != null) {
            vehicle.setOrganization(organizationRepository.findById(vehicle.getOrganization().getId())
                    .orElseThrow(() -> new RuntimeException("Organization not found")));
        }
        return vehicleRepository.save(vehicle);
    }

    public Vehicle getVehicle(String vin) {
        return vehicleRepository.findById(vin).orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }
}
