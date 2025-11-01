package org.example.controller;

import java.util.List;
import java.util.Map;

import org.example.models.dto.request.OemManufacturerCreateRequest;
import org.example.models.dto.response.ApiErrorResponse;
import org.example.models.dto.response.OemManufacturerResponse;
import org.example.service.IService.IOemManufacturerService;
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
@RequestMapping("/api/oem-manufacturers")
@Tag(name = "OEM Manufacturers", description = "OEM manufacturer management including creation, search, and manufacturer information")
public class OemManufacturersController {

    @Autowired
    private IOemManufacturerService oemManufacturerService;

    @PostMapping
    @Operation(summary = "Create New OEM Manufacturer", description = "Create a new OEM manufacturer with code, name, and contact information. Roles: Admin, EVM_Staff.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OEM manufacturer created successfully", content = @Content(schema = @Schema(implementation = OemManufacturerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "OEM code or name already exists", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> createOemManufacturer(
            @Valid @RequestBody OemManufacturerCreateRequest request) {
        try {
            OemManufacturerResponse response = oemManufacturerService.createOemManufacturer(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            ApiErrorResponse error = ApiErrorResponse.badRequest(e.getMessage(), "/api/oem-manufacturers");
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to create OEM manufacturer",
                    "/api/oem-manufacturers");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get OEM Manufacturer by ID", description = "Retrieve detailed information about a specific OEM manufacturer by its unique identifier. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OEM manufacturer retrieved successfully", content = @Content(schema = @Schema(implementation = OemManufacturerResponse.class))),
            @ApiResponse(responseCode = "404", description = "OEM manufacturer not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> getOemManufacturerById(
            @PathVariable @Parameter(description = "OEM Manufacturer ID", required = true, example = "1") Long id) {
        try {
            OemManufacturerResponse response = oemManufacturerService.getOemManufacturerById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to retrieve OEM manufacturer",
                    "/api/oem-manufacturers");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/by-code/{code}")
    @Operation(summary = "Get OEM Manufacturer by Code", description = "Retrieve OEM manufacturer information using the manufacturer code. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OEM manufacturer retrieved successfully", content = @Content(schema = @Schema(implementation = OemManufacturerResponse.class))),
            @ApiResponse(responseCode = "404", description = "OEM manufacturer not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> getOemManufacturerByCode(
            @PathVariable @Parameter(description = "OEM Code", required = true, example = "TESLA") String code) {
        try {
            OemManufacturerResponse response = oemManufacturerService.getOemManufacturerByCode(code);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to retrieve OEM manufacturer",
                    "/api/oem-manufacturers");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update OEM Manufacturer", description = "Update existing OEM manufacturer details including code, name, and contact information. Roles: Admin, EVM_Staff.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OEM manufacturer updated successfully", content = @Content(schema = @Schema(implementation = OemManufacturerResponse.class))),
            @ApiResponse(responseCode = "404", description = "OEM manufacturer not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "OEM code or name already exists", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> updateOemManufacturer(
            @PathVariable @Parameter(description = "OEM Manufacturer ID", required = true, example = "1") Long id,
            @Valid @RequestBody OemManufacturerCreateRequest request) {
        try {
            OemManufacturerResponse response = oemManufacturerService.updateOemManufacturer(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to update OEM manufacturer",
                    "/api/oem-manufacturers");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete OEM Manufacturer", description = "Remove an OEM manufacturer from the system. Roles: Admin, EVM_Staff. Cannot delete manufacturers referenced by service centers or warranty policies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OEM manufacturer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "OEM manufacturer not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Cannot delete manufacturer due to business rules", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> deleteOemManufacturer(
            @PathVariable @Parameter(description = "OEM Manufacturer ID", required = true, example = "1") Long id) {
        try {
            boolean deleted = oemManufacturerService.deleteOemManufacturer(id);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to delete OEM manufacturer",
                        "/api/oem-manufacturers");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            ApiErrorResponse error = ApiErrorResponse.conflict(e.getMessage(), "/api/oem-manufacturers");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to delete OEM manufacturer",
                    "/api/oem-manufacturers");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping
    @Operation(summary = "List All OEM Manufacturers", description = "Retrieve a paginated list of all OEM manufacturers in the system. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OEM manufacturers retrieved successfully", content = @Content(schema = @Schema(implementation = OemManufacturerResponse.class)))
    })
    public ResponseEntity<?> getAllOemManufacturers(
            @RequestParam(defaultValue = "10") @Parameter(description = "Number of records to return") int limit,
            @RequestParam(defaultValue = "0") @Parameter(description = "Number of records to skip") int offset) {
        try {
            List<OemManufacturerResponse> manufacturers = oemManufacturerService.getAllOemManufacturers(limit, offset);
            return ResponseEntity.ok(manufacturers);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to retrieve OEM manufacturers",
                    "/api/oem-manufacturers");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search OEM Manufacturers", description = "Search OEM manufacturers by name, code, or contact information using keywords. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully", content = @Content(schema = @Schema(implementation = OemManufacturerResponse.class)))
    })
    public ResponseEntity<?> searchOemManufacturers(
            @RequestParam @Parameter(description = "Search keyword", required = true, example = "tesla") String keyword,
            @RequestParam(defaultValue = "10") @Parameter(description = "Number of records to return") int limit,
            @RequestParam(defaultValue = "0") @Parameter(description = "Number of records to skip") int offset) {
        try {
            List<OemManufacturerResponse> manufacturers = oemManufacturerService.searchOemManufacturers(keyword, limit,
                    offset);
            return ResponseEntity.ok(manufacturers);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to search OEM manufacturers",
                    "/api/oem-manufacturers");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/service-center/{serviceCenterId}")
    @Operation(summary = "Get OEM Manufacturers by Service Center", description = "Retrieve all OEM manufacturers associated with a specific service center. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OEM manufacturers retrieved successfully", content = @Content(schema = @Schema(implementation = OemManufacturerResponse.class)))
    })
    public ResponseEntity<?> getOemManufacturersByServiceCenter(
            @PathVariable @Parameter(description = "Service Center ID", required = true, example = "1") Long serviceCenterId,
            @RequestParam(defaultValue = "10") @Parameter(description = "Number of records to return") int limit,
            @RequestParam(defaultValue = "0") @Parameter(description = "Number of records to skip") int offset) {
        try {
            List<OemManufacturerResponse> manufacturers = oemManufacturerService
                    .getOemManufacturersByServiceCenter(serviceCenterId, limit, offset);
            return ResponseEntity.ok(manufacturers);
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to retrieve OEM manufacturers",
                    "/api/oem-manufacturers");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/exists/code/{code}")
    @Operation(summary = "Check if OEM Code Exists", description = "Check if an OEM manufacturer code already exists in the system. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Code existence check completed")
    })
    public ResponseEntity<?> checkOemCodeExists(
            @PathVariable @Parameter(description = "OEM Code", required = true, example = "TESLA") String code) {
        try {
            boolean exists = oemManufacturerService.existsByCode(code);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to check OEM code existence",
                    "/api/oem-manufacturers");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/exists/name/{name}")
    @Operation(summary = "Check if OEM Name Exists", description = "Check if an OEM manufacturer name already exists in the system. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Name existence check completed")
    })
    public ResponseEntity<?> checkOemNameExists(
            @PathVariable @Parameter(description = "OEM Name", required = true, example = "Tesla Inc.") String name) {
        try {
            boolean exists = oemManufacturerService.existsByName(name);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to check OEM name existence",
                    "/api/oem-manufacturers");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/count")
    @Operation(summary = "Get OEM Manufacturer Count", description = "Get the total number of OEM manufacturers in the system. Roles: Admin, SC_Staff, SC_Technician, EVM_Staff.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public ResponseEntity<?> getOemManufacturerCount() {
        try {
            Long count = oemManufacturerService.countOemManufacturers();
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            ApiErrorResponse error = ApiErrorResponse.internalServerError("Failed to get OEM manufacturer count",
                    "/api/oem-manufacturers");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
