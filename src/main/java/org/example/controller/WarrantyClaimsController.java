package org.example.controller;

import java.util.List;
import java.util.Map;

import org.example.models.dto.request.ApproveClaimRequest;
import org.example.models.dto.request.CreateWarrantyClaimRequest;
import org.example.models.dto.request.RejectClaimRequest;
import org.example.models.dto.request.TechnicianAssignmentRequest;
import org.example.models.dto.response.WarrantyClaimDetailResponse;
import org.example.models.dto.response.WarrantyClaimResponse;
import org.example.models.enums.ClaimStatus;
import org.example.models.enums.UserRole;
import org.example.service.IService.IAuthenticationService;
import org.example.service.IService.IWarrantyClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/claims")
@Tag(name = "Warranty Claims", description = "Warranty claim processing including creation, submission, approval, and rejection")
public class WarrantyClaimsController {

    @Autowired
    private IWarrantyClaimService warrantyClaimService;

    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping
    @Operation(summary = "Create Draft Warranty Claim", description = "Create a new draft warranty claim for a vehicle. The claim can be submitted later for processing.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Warranty claim creation data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Create Claim Example", value = "{\"vehicleId\": 1, \"description\": \"Battery not holding charge\", \"dtcCode\": \"P0A80\", \"vin\": \"1HGBH41JXMN109186\"}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Draft claim created successfully", content = @Content(schema = @Schema(implementation = WarrantyClaimDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<?> createDraftClaim(@RequestBody CreateWarrantyClaimRequest body) {
        try {
            // RBAC: Admin, EVM_Staff, SC_Staff can create draft claims
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            Object roleObj = session.get("role");
            if (!(roleObj instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Insufficient permissions"));
            }
            UserRole currentRole = (UserRole) roleObj;
            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff
                    && currentRole != UserRole.SC_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only Admin, EVM_Staff or SC_Staff can create claims"));
            }
            // Service path expects VIN; for now derive from repository by vehicleId is not
            // available, use request vehicleId flow
            // If VIN is required, adapt service signature. Here we pass a placeholder VIN
            // not used by service save, only for validation.
            String vehicleVin = "VIN_PLACEHOLDER";
            WarrantyClaimDetailResponse res = warrantyClaimService.createDraftClaimByVin(vehicleVin, body);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Claim Details", description = "Retrieve detailed information about a specific warranty claim including status and history.", parameters = {
            @Parameter(name = "id", description = "Claim ID", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Claim details retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyClaimDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "Claim not found")
    })
    public ResponseEntity<?> getClaimDetail(@PathVariable("id") Long id) {
        try {
            WarrantyClaimDetailResponse res = warrantyClaimService.getClaimDetail(id);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit Claim", description = "Submit a draft warranty claim to EVM (Electric Vehicle Manufacturer) for review and processing.", parameters = {
            @Parameter(name = "id", description = "Claim ID to submit", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Claim submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid claim or submission failed")
    })
    public ResponseEntity<?> submitClaim(@PathVariable("id") Long id) {
        try {
            // RBAC: Admin, SC_Staff can submit to EVM
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            Object roleObj = session.get("role");
            if (!(roleObj instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Insufficient permissions"));
            }
            UserRole currentRole = (UserRole) roleObj;
            if (currentRole != UserRole.Admin && currentRole != UserRole.SC_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only Admin or SC_Staff can submit claims"));
            }
            boolean success = warrantyClaimService.submitClaimToEvm(id);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClaim(@PathVariable("id") Long id,
            @RequestBody org.example.models.dto.request.WarrantyClaimUpdateRequest body) {
        try {
            // RBAC: Admin, EVM_Staff, SC_Staff can update claim basic fields; status
            // transitions checked separately
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            Object roleObj = session.get("role");
            if (!(roleObj instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Insufficient permissions"));
            }
            UserRole currentRole = (UserRole) roleObj;
            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff
                    && currentRole != UserRole.SC_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only Admin, EVM_Staff or SC_Staff can update claims"));
            }
            boolean ok = true;
            if (body.getStatus() != null) {
                // Only Admin/EVM_Staff can set to approved/rejected; SC_Staff can set submitted
                if (body.getStatus() == ClaimStatus.approved || body.getStatus() == ClaimStatus.rejected) {
                    if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(Map.of("error", "Only Admin or EVM_Staff can approve/reject"));
                    }
                }
                Long userId = (Long) session.get("userId");
                ok = warrantyClaimService.updateClaimStatus(id, body.getStatus(), null, userId);
            }
            return ResponseEntity.ok(Map.of("success", ok));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve Claim", description = "Approve a warranty claim with approval notes and approver information.", parameters = {
            @Parameter(name = "id", description = "Claim ID to approve", required = true, example = "1")
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Approval data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Approve Claim Example", value = "{\"approvedBy\": 1, \"notes\": \"Approved for battery replacement\"}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Claim approved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid approval data")
    })
    public ResponseEntity<?> approveClaim(@PathVariable("id") Long id,
            @RequestBody ApproveClaimRequest body) {
        try {
            // RBAC: Only Admin or EVM_Staff can approve
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            Object roleObj = session.get("role");
            if (!(roleObj instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Insufficient permissions"));
            }
            UserRole currentRole = (UserRole) roleObj;
            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only Admin or EVM_Staff can approve claims"));
            }
            Long approverId = (Long) session.get("userId");
            String approvalNotes = body.getNote();
            boolean success = warrantyClaimService.approveClaim(id, approverId,
                    approvalNotes != null ? approvalNotes : "Approved");
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject Claim", description = "Reject a warranty claim with rejection reason and rejector information.", parameters = {
            @Parameter(name = "id", description = "Claim ID to reject", required = true, example = "1")
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Rejection data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Reject Claim Example", value = "{\"rejectedBy\": 1, \"reason\": \"Not covered under warranty\"}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Claim rejected successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid rejection data")
    })
    public ResponseEntity<?> rejectClaim(@PathVariable("id") Long id,
            @RequestBody RejectClaimRequest body) {
        try {
            // RBAC: Only Admin or EVM_Staff can reject
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            Object roleObj = session.get("role");
            if (!(roleObj instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Insufficient permissions"));
            }
            UserRole currentRole = (UserRole) roleObj;
            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only Admin or EVM_Staff can reject claims"));
            }
            Long rejectedById = (Long) session.get("userId");
            String rejectionReason = body.getRejectionReason();
            boolean success = warrantyClaimService.rejectClaim(id,
                    rejectionReason != null ? rejectionReason : "Rejected", rejectedById);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "List Warranty Claims", description = "Retrieve a list of warranty claims with optional filtering by status, vehicle, or service center.", parameters = {
            @Parameter(name = "status", description = "Filter by claim status", required = false, example = "DRAFT", schema = @Schema(allowableValues = {
                    "DRAFT", "SUBMITTED", "APPROVED", "REJECTED", "COMPLETED" })),
            @Parameter(name = "vehicleId", description = "Filter by vehicle ID", required = false, example = "1"),
            @Parameter(name = "serviceCenterId", description = "Filter by service center ID", required = false, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Claims retrieved successfully", content = @Content(schema = @Schema(implementation = WarrantyClaimResponse.class))),
            @ApiResponse(responseCode = "404", description = "No claims found")
    })
    public ResponseEntity<?> listClaims(@RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "vehicleId", required = false) Long vehicleId,
            @RequestParam(value = "serviceCenterId", required = false) Long serviceCenterId) {
        try {
            List<WarrantyClaimResponse> claims;
            if (vehicleId != null) {
                claims = warrantyClaimService.getClaimHistory(vehicleId);
            } else if (serviceCenterId != null) {
                ClaimStatus claimStatus = status != null ? ClaimStatus.valueOf(status.toUpperCase()) : null;
                claims = warrantyClaimService.getClaimsByServiceCenter(serviceCenterId, claimStatus);
            } else {
                // Default to getting claims by status
                ClaimStatus claimStatus = status != null ? ClaimStatus.valueOf(status.toUpperCase())
                        : ClaimStatus.draft;
                claims = warrantyClaimService.getClaimsByStatus(claimStatus, 1L); // Default organization
            }
            return ResponseEntity.ok(claims);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/assign")
    @Operation(summary = "Assign Technician to Claim", description = "Assign a technician to handle a specific warranty claim.", parameters = {
            @Parameter(name = "id", description = "Claim ID to assign technician to", required = true, example = "1")
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Technician assignment data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Assign Technician Example", value = "{\"technicianId\": 123, \"assignmentNotes\": \"Senior technician with battery expertise\", \"estimatedHours\": 8}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Technician assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid assignment data or claim not found")
    })
    public ResponseEntity<?> assignTechnician(@PathVariable("id") Long id,
            @RequestBody TechnicianAssignmentRequest body) {
        try {
            // RBAC: Only Admin, EVM_Staff, SC_Staff can assign technicians
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Missing or invalid Authorization header"));
            }
            String token = authHeader.substring("Bearer ".length()).trim();
            Map<String, Object> session = authenticationService.getSessionByToken(token);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session"));
            }
            Object roleObj = session.get("role");
            if (!(roleObj instanceof UserRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Insufficient permissions"));
            }
            UserRole currentRole = (UserRole) roleObj;
            if (currentRole != UserRole.Admin && currentRole != UserRole.EVM_Staff
                    && currentRole != UserRole.SC_Staff) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only Admin, EVM_Staff or SC_Staff can assign technicians"));
            }

            // Validate request body
            if (body.getTechnicianId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Technician ID is required"));
            }

            // Set the claim ID in the request
            body.setClaimId(id);

            boolean success = warrantyClaimService.assignTechnicianToClaim(id, body);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
