package org.example.controller;

import java.util.List;
import java.util.Map;

import org.example.models.dto.request.ServiceCenterCreateRequest;
import org.example.models.dto.response.ServiceCenterResponse;
import org.example.service.IService.IServiceCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/service-centers")
@Tag(name = "Service Centers", description = "Service center management including creation, activation, and regional operations")
public class ServiceCentersController {

    @Autowired
    private IServiceCenterService serviceCenterService;

    @PostMapping
    @Operation(summary = "Create Service Center", description = "Create a new service center with location, contact information, and regional details.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Service center creation data", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Create Service Center Example", value = "{\"code\": \"SC001\", \"name\": \"Downtown Service Center\", \"region\": \"North\", \"email\": \"downtown@service.com\", \"phone\": \"+1234567890\", \"address\": \"123 Main St, City, State\"}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service center created successfully", content = @Content(schema = @Schema(implementation = ServiceCenterResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<?> createServiceCenter(@RequestBody Map<String, Object> body) {
        try {
            ServiceCenterCreateRequest req = new ServiceCenterCreateRequest();
            Object code = body.get("code");
            Object name = body.get("name");
            Object region = body.get("region");
            Object email = body.get("email");
            Object phone = body.get("phone");
            Object address = body.get("address");

            req.setCode(code != null ? String.valueOf(code) : null);
            req.setName(name != null ? String.valueOf(name) : null);
            req.setRegion(region != null ? String.valueOf(region) : null);
            req.setEmail(email != null ? String.valueOf(email) : null);
            req.setPhone(phone != null ? String.valueOf(phone) : null);
            req.setAddress(address != null ? String.valueOf(address) : null);

            ServiceCenterResponse res = serviceCenterService.createServiceCenter(req);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Service Center by ID", description = "Retrieve detailed information about a specific service center by its ID.", parameters = {
            @Parameter(name = "id", description = "Service center ID", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service center retrieved successfully", content = @Content(schema = @Schema(implementation = ServiceCenterResponse.class))),
            @ApiResponse(responseCode = "404", description = "Service center not found")
    })
    public ResponseEntity<?> getServiceCenterById(@PathVariable("id") Long id) {
        try {
            ServiceCenterResponse res = serviceCenterService.getServiceCenterById(id);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/region/{region}")
    @Operation(summary = "Get Service Centers by Region", description = "Retrieve all service centers in a specific region.", parameters = {
            @Parameter(name = "region", description = "Region name", required = true, example = "North")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service centers retrieved successfully", content = @Content(schema = @Schema(implementation = ServiceCenterResponse.class))),
            @ApiResponse(responseCode = "404", description = "No service centers found in region")
    })
    public ResponseEntity<?> getServiceCentersByRegion(@PathVariable("region") String region) {
        try {
            List<ServiceCenterResponse> res = serviceCenterService.getServiceCentersByRegion(region);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get Service Center by Code", description = "Retrieve a specific service center by its unique code.", parameters = {
            @Parameter(name = "code", description = "Service center code", required = true, example = "SC001")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service center retrieved successfully", content = @Content(schema = @Schema(implementation = ServiceCenterResponse.class))),
            @ApiResponse(responseCode = "404", description = "Service center not found")
    })
    public ResponseEntity<?> getServiceCenterByCode(@PathVariable("code") String code) {
        try {
            ServiceCenterResponse res = serviceCenterService.getServiceCenterByCode(code);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<?> activateServiceCenter(@PathVariable("id") Long id) {
        try {
            boolean success = serviceCenterService.activateServiceCenter(id);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateServiceCenter(@PathVariable("id") Long id,
            @RequestParam(value = "reason", required = false) String reason) {
        try {
            boolean success = serviceCenterService.deactivateServiceCenter(id,
                    reason != null ? reason : "Manual deactivation");
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/oem/{oemId}")
    public ResponseEntity<?> getServiceCentersByOem(@PathVariable("oemId") Long oemId) {
        try {
            List<ServiceCenterResponse> res = serviceCenterService.getServiceCentersByOem(oemId);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search Service Centers", description = "Search service centers by keyword, region, or OEM with flexible filtering options.", parameters = {
            @Parameter(name = "keyword", description = "Search keyword for name or address", required = false, example = "downtown"),
            @Parameter(name = "region", description = "Filter by region", required = false, example = "North"),
            @Parameter(name = "oemId", description = "Filter by OEM ID", required = false, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service centers retrieved successfully", content = @Content(schema = @Schema(implementation = ServiceCenterResponse.class))),
            @ApiResponse(responseCode = "404", description = "No service centers found")
    })
    public ResponseEntity<?> searchServiceCenters(@RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "oemId", required = false) Long oemId) {
        try {
            List<ServiceCenterResponse> res = serviceCenterService.searchServiceCenters(keyword, region, oemId);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}
