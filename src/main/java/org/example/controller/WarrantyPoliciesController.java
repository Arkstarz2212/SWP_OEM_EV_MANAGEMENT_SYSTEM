package org.example.controller;

import java.util.List;

import org.example.models.dto.request.WarrantyPolicyCreateRequest;
import org.example.models.dto.response.ApiErrorResponse;
import org.example.models.dto.response.WarrantyPolicyResponse;
import org.example.service.IService.IWarrantyPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/warranty-policies")
@Tag(name = "Warranty Policies", description = "Warranty policy management for OEM manufacturers including creation, search, and policy analysis")
public class WarrantyPoliciesController {

    @Autowired
    private IWarrantyPolicyService warrantyPolicyService;

    @PostMapping
    @Operation(summary = "Create New Warranty Policy", description = "Create a new warranty policy for a specific OEM manufacturer with coverage details and effective dates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Warranty policy created successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Policy code already exists", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> createWarrantyPolicy(
            @Valid @RequestBody WarrantyPolicyCreateRequest request,
            @RequestParam(defaultValue = "1") @Parameter(description = "OEM Manufacturer ID") Long oemId) {
        try {
            WarrantyPolicyResponse response = warrantyPolicyService.createWarrantyPolicy(request, oemId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            ApiErrorResponse error = ApiErrorResponse.badRequest(e.getMessage(), "/api/warranty-policies");
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to create warranty policy",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Warranty Policy by ID", description = "Retrieve detailed information about a specific warranty policy by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policy retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> getWarrantyPolicyById(
            @PathVariable @Parameter(description = "Warranty Policy ID", required = true, example = "1") Long id) {
        try {
            WarrantyPolicyResponse response = warrantyPolicyService.getWarrantyPolicyById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiErrorResponse error = ApiErrorResponse.notFound(e.getMessage(), "/api/warranty-policies");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to retrieve warranty policy",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/by-code/{policyCode}")
    @Operation(summary = "Get Warranty Policy by Code", description = "Retrieve warranty policy information using the policy code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policy retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> getWarrantyPolicyByCode(
            @PathVariable @Parameter(description = "Policy Code", required = true, example = "ModelA_Battery") String policyCode) {
        try {
            WarrantyPolicyResponse response = warrantyPolicyService.getWarrantyPolicyByCode(policyCode);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiErrorResponse error = ApiErrorResponse.notFound(e.getMessage(), "/api/warranty-policies");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to retrieve warranty policy",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Warranty Policy", description = "Update existing warranty policy details including coverage terms and effective dates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policy updated successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> updateWarrantyPolicy(
            @PathVariable @Parameter(description = "Warranty Policy ID", required = true, example = "1") Long id,
            @Valid @RequestBody WarrantyPolicyCreateRequest request) {
        try {
            WarrantyPolicyResponse response = warrantyPolicyService.updateWarrantyPolicy(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiErrorResponse error = ApiErrorResponse.notFound(e.getMessage(), "/api/warranty-policies");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to update warranty policy",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Warranty Policy", description = "Remove a warranty policy from the system. Cannot delete default policies or those referenced by active claims.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policy deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Cannot delete policy due to business rules", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> deleteWarrantyPolicy(
            @PathVariable @Parameter(description = "Warranty Policy ID", required = true, example = "1") Long id) {
        try {
            boolean deleted = warrantyPolicyService.deleteWarrantyPolicy(id);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to delete warranty policy",
                        "/api/warranty-policies");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        } catch (IllegalArgumentException e) {
            ApiErrorResponse error = ApiErrorResponse.notFound(e.getMessage(), "/api/warranty-policies");
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            ApiErrorResponse error = ApiErrorResponse.conflict(e.getMessage(), "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to delete warranty policy",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping
    @Operation(summary = "List All Warranty Policies", description = "Retrieve a paginated list of all warranty policies in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policies retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class)))
    })
    public ResponseEntity<?> getAllWarrantyPolicies(
            @RequestParam(defaultValue = "10") @Parameter(description = "Number of records to return") int limit,
            @RequestParam(defaultValue = "0") @Parameter(description = "Number of records to skip") int offset) {
        try {
            List<WarrantyPolicyResponse> policies = warrantyPolicyService.getAllWarrantyPolicies(limit, offset);
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to retrieve warranty policies",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/oem/{oemId}")
    @Operation(summary = "Get Warranty Policies by OEM", description = "Retrieve all warranty policies for a specific OEM manufacturer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warranty policies retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class)))
    })
    public ResponseEntity<?> getWarrantyPoliciesByOem(
            @PathVariable @Parameter(description = "OEM Manufacturer ID", required = true, example = "1") Long oemId,
            @RequestParam(defaultValue = "10") @Parameter(description = "Number of records to return") int limit,
            @RequestParam(defaultValue = "0") @Parameter(description = "Number of records to skip") int offset) {
        try {
            List<WarrantyPolicyResponse> policies = warrantyPolicyService.getWarrantyPoliciesByOem(oemId, limit,
                    offset);
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to retrieve warranty policies",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/active")
    @Operation(summary = "Get Active Warranty Policies", description = "Retrieve all currently active warranty policies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active warranty policies retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class)))
    })
    public ResponseEntity<?> getActiveWarrantyPolicies(
            @RequestParam(defaultValue = "10") @Parameter(description = "Number of records to return") int limit,
            @RequestParam(defaultValue = "0") @Parameter(description = "Number of records to skip") int offset) {
        try {
            List<WarrantyPolicyResponse> policies = warrantyPolicyService.getActiveWarrantyPolicies(limit, offset);
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to retrieve active warranty policies",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/oem/{oemId}/active")
    @Operation(summary = "Get Active Warranty Policies by OEM", description = "Retrieve all active warranty policies for a specific OEM manufacturer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active warranty policies retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class)))
    })
    public ResponseEntity<?> getActiveWarrantyPoliciesByOem(
            @PathVariable @Parameter(description = "OEM Manufacturer ID", required = true, example = "1") Long oemId,
            @RequestParam(defaultValue = "10") @Parameter(description = "Number of records to return") int limit,
            @RequestParam(defaultValue = "0") @Parameter(description = "Number of records to skip") int offset) {
        try {
            List<WarrantyPolicyResponse> policies = warrantyPolicyService.getActiveWarrantyPoliciesByOem(oemId, limit,
                    offset);
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to retrieve active warranty policies",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search Warranty Policies", description = "Search warranty policies by policy name or code using keywords.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class)))
    })
    public ResponseEntity<?> searchWarrantyPolicies(
            @RequestParam @Parameter(description = "Search keyword", required = true, example = "battery") String keyword,
            @RequestParam(defaultValue = "10") @Parameter(description = "Number of records to return") int limit,
            @RequestParam(defaultValue = "0") @Parameter(description = "Number of records to skip") int offset) {
        try {
            List<WarrantyPolicyResponse> policies = warrantyPolicyService.searchWarrantyPolicies(keyword, limit,
                    offset);
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to search warranty policies",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/oem/{oemId}/default")
    @Operation(summary = "Get Default Policy by OEM", description = "Retrieve the default warranty policy for a specific OEM manufacturer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Default warranty policy retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class))),
            @ApiResponse(responseCode = "404", description = "No default policy found for OEM", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> getDefaultPolicyByOem(
            @PathVariable @Parameter(description = "OEM Manufacturer ID", required = true, example = "1") Long oemId) {
        try {
            WarrantyPolicyResponse response = warrantyPolicyService.getDefaultPolicyByOem(oemId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiErrorResponse error = ApiErrorResponse.notFound(e.getMessage(), "/api/warranty-policies");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to retrieve default warranty policy",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}/set-default")
    @Operation(summary = "Set Default Policy", description = "Set a warranty policy as the default policy for its OEM manufacturer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policy set as default successfully"),
            @ApiResponse(responseCode = "404", description = "Warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> setDefaultPolicy(
            @PathVariable @Parameter(description = "Warranty Policy ID", required = true, example = "1") Long id,
            @RequestParam(defaultValue = "1") @Parameter(description = "OEM Manufacturer ID") Long oemId) {
        try {
            boolean success = warrantyPolicyService.setDefaultPolicy(id, oemId);
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to set default policy",
                        "/api/warranty-policies");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        } catch (IllegalArgumentException e) {
            ApiErrorResponse error = ApiErrorResponse.notFound(e.getMessage(), "/api/warranty-policies");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to set default policy",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update Policy Status", description = "Activate or deactivate a warranty policy.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policy status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Warranty policy not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> updatePolicyStatus(
            @PathVariable @Parameter(description = "Warranty Policy ID", required = true, example = "1") Long id,
            @RequestParam @Parameter(description = "Active status", required = true) boolean isActive) {
        try {
            boolean success = warrantyPolicyService.updateWarrantyPolicyStatus(id, isActive);
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to update policy status",
                        "/api/warranty-policies");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        } catch (IllegalArgumentException e) {
            ApiErrorResponse error = ApiErrorResponse.notFound(e.getMessage(), "/api/warranty-policies");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to update policy status",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/analytics/coverage")
    @Operation(summary = "Get Policies by Coverage", description = "Retrieve warranty policies filtered by coverage duration or mileage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policies retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class)))
    })
    public ResponseEntity<?> getPoliciesByCoverage(
            @RequestParam(required = false) @Parameter(description = "Minimum warranty months") Integer minMonths,
            @RequestParam(required = false) @Parameter(description = "Maximum warranty months") Integer maxMonths,
            @RequestParam(required = false) @Parameter(description = "Minimum warranty km") Integer minKm,
            @RequestParam(required = false) @Parameter(description = "Maximum warranty km") Integer maxKm) {
        try {
            List<WarrantyPolicyResponse> policies;
            if (minMonths != null || maxMonths != null) {
                policies = warrantyPolicyService.getPoliciesByWarrantyMonths(minMonths, maxMonths);
            } else if (minKm != null || maxKm != null) {
                policies = warrantyPolicyService.getPoliciesByWarrantyKm(minKm, maxKm);
            } else {
                policies = warrantyPolicyService.getAllWarrantyPolicies(100, 0);
            }
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to retrieve policies by coverage",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/oem/{oemId}/analytics/comparison")
    @Operation(summary = "Compare Policies by OEM", description = "Retrieve and compare all warranty policies for an OEM manufacturer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policy comparison retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class)))
    })
    public ResponseEntity<?> comparePoliciesByOem(
            @PathVariable @Parameter(description = "OEM Manufacturer ID", required = true, example = "1") Long oemId) {
        try {
            List<WarrantyPolicyResponse> policies = warrantyPolicyService.comparePoliciesByOem(oemId);
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to compare policies",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/oem/{oemId}/analytics/most-generous")
    @Operation(summary = "Get Most Generous Policy by OEM", description = "Find the warranty policy with the most generous coverage terms for an OEM.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Most generous policy retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyPolicyResponse.class))),
            @ApiResponse(responseCode = "404", description = "No active policies found for OEM", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> getMostGenerousPolicyByOem(
            @PathVariable @Parameter(description = "OEM Manufacturer ID", required = true, example = "1") Long oemId) {
        try {
            WarrantyPolicyResponse response = warrantyPolicyService.getMostGenerousPolicyByOem(oemId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiErrorResponse error = ApiErrorResponse.notFound(e.getMessage(), "/api/warranty-policies");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to retrieve most generous policy",
                    "/api/warranty-policies");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
