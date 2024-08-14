package com.moretorque.service;

import com.moretorque.model.Vehicle;
import com.moretorque.repository.OrganizationRepository;
import com.moretorque.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
// in mem caching using concurrent hash map
@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final OrganizationRepository organizationRepository;
    private final NhtsaService nhtsaService;
    private final ConcurrentHashMap<String, CacheEntry> vinCache;

    public VehicleService(VehicleRepository vehicleRepository, OrganizationRepository organizationRepository, NhtsaService nhtsaService) {
        this.vehicleRepository = vehicleRepository;
        this.organizationRepository = organizationRepository;
        this.nhtsaService = nhtsaService;
        this.vinCache = new ConcurrentHashMap<>();
    }

    public String decodeVin(String vin) {
        // Check cache first
        CacheEntry cacheEntry = vinCache.get(vin);
        if (cacheEntry != null && !cacheEntry.isExpired()) {
            return cacheEntry.getData();
        }

        // If not in cache or expired, call NHTSA service
        String decodedData = nhtsaService.decodeVin(vin);

        // Store in cache
        vinCache.put(vin, new CacheEntry(decodedData));

        return decodedData;
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

    private static class CacheEntry {
        private final String data;
        private final long timestamp;
        private static final long CACHE_DURATION = TimeUnit.HOURS.toMillis(24); // 24 hour cache

        public CacheEntry(String data) {
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }

        public String getData() {
            return data;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_DURATION;
        }
    }
}
