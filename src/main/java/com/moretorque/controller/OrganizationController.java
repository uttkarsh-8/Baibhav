package com.moretorque.controller;

import com.moretorque.model.Organization;
import com.moretorque.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orgs")
@PreAuthorize("isAuthenticated()")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    public ResponseEntity<Organization> createOrganization(@Valid @RequestBody Organization organization) {
        Organization createdOrg = organizationService.createOrganization(organization);
        return new ResponseEntity<>(createdOrg, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> orgs = organizationService.getAllOrganizations();
        return ResponseEntity.ok(orgs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id) {
        return organizationService.getOrganizationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable Long id, @RequestBody Organization organization) {
        Organization updatedOrg = organizationService.updateOrganization(id, organization);
        return ResponseEntity.ok(updatedOrg);
    }
}
