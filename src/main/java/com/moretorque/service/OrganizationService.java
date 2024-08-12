package com.moretorque.service;

import com.moretorque.model.Organization;
import com.moretorque.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Transactional
    public Organization createOrganization(Organization organization) {
        if (organization.getFuelReimbursementPolicy() == null) {
            organization.setFuelReimbursementPolicy("1000"); // Default value
        }
        return organizationRepository.save(organization);
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Optional<Organization> getOrganizationById(Long id) {
        return organizationRepository.findById(id);
    }

    @Transactional
    public Organization updateOrganization(Long id, Organization updatedOrg) {
        return organizationRepository.findById(id)
                .map(org -> {
                    org.setName(updatedOrg.getName());
                    org.setAccount(updatedOrg.getAccount());
                    org.setWebsite(updatedOrg.getWebsite());

                    updateFuelReimbursementPolicy(org, updatedOrg.getFuelReimbursementPolicy());
                    updateSpeedLimitPolicy(org, updatedOrg.getSpeedLimitPolicy());

                    return organizationRepository.save(org);
                })
                .orElseThrow(() -> new RuntimeException("Organization not found with id " + id));
    }

    private void updateFuelReimbursementPolicy(Organization org, String newPolicy) {
        if (newPolicy != null && !newPolicy.equals(org.getFuelReimbursementPolicy())) {
            org.setFuelReimbursementPolicy(newPolicy);
            propagateFuelReimbursementPolicy(org, newPolicy);
        }
    }

    private void propagateFuelReimbursementPolicy(Organization org, String policy) {
        for (Organization child : org.getChildren()) {
            if (child.getFuelReimbursementPolicy() == null) {
                child.setFuelReimbursementPolicy(policy);
                propagateFuelReimbursementPolicy(child, policy);
            }
        }
    }

    private void updateSpeedLimitPolicy(Organization org, String newPolicy) {
        if (newPolicy != null && !newPolicy.equals(org.getSpeedLimitPolicy())) {
            org.setSpeedLimitPolicy(newPolicy);
            // Speed limit policy is not propagated to children
        }
    }
}