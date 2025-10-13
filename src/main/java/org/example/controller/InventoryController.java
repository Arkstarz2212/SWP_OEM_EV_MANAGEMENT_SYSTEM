package org.example.controller;

import java.util.List;
import java.util.Map;

import org.example.service.IService.IInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory", description = "Inventory operations for parts: quantity, reservation, release, and transfer")
public class InventoryController {

    @Autowired
    private IInventoryService inventoryService;

    @GetMapping("/parts/{partNumber}")
    @Operation(summary = "Get Part Inventory", description = "Get quantity on hand for a part at a specific location.", parameters = {
            @Parameter(name = "partNumber", description = "Part number", required = true, example = "BAT001"),
            @Parameter(name = "locationId", description = "Inventory location ID", required = true, example = "100")
    })
    public ResponseEntity<?> getPartInventory(@PathVariable("partNumber") String partNumber,
            @RequestParam("locationId") Long locationId) {
        try {
            Integer quantity = inventoryService.getPartQuantity(locationId, partNumber);
            return ResponseEntity.ok(Map.of("partNumber", partNumber, "quantity", quantity));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/parts/{partNumber}/quantity")
    @Operation(summary = "Update Part Quantity", description = "Adjust quantity for a part at a location.", parameters = {
            @Parameter(name = "partNumber", description = "Part number", required = true, example = "BAT001"),
            @Parameter(name = "locationId", description = "Inventory location ID", required = true, example = "100"),
            @Parameter(name = "quantity", description = "New quantity value", required = true, example = "50")
    })
    public ResponseEntity<?> updatePartQuantity(@PathVariable("partNumber") String partNumber,
            @RequestParam("locationId") Long locationId,
            @RequestParam("quantity") Integer quantity) {
        try {
            boolean success = inventoryService.updatePartQuantity(locationId, partNumber, quantity);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/parts/{partNumber}/reserve")
    @Operation(summary = "Reserve Parts for Claim", description = "Reserve a quantity of a part for a specific warranty claim.", parameters = {
            @Parameter(name = "partNumber", description = "Part number", required = true, example = "BAT001"),
            @Parameter(name = "claimId", description = "Warranty claim ID", required = true, example = "2001"),
            @Parameter(name = "quantity", description = "Quantity to reserve", required = true, example = "2")
    })
    public ResponseEntity<?> reservePart(@PathVariable("partNumber") String partNumber,
            @RequestParam("claimId") Long claimId,
            @RequestParam("quantity") Integer quantity) {
        try {
            boolean success = inventoryService.reservePartsForClaim(claimId, partNumber, quantity);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/parts/{partNumber}/release")
    @Operation(summary = "Release Reserved Parts", description = "Release previously reserved parts for a claim.", parameters = {
            @Parameter(name = "partNumber", description = "Part number", required = true, example = "BAT001"),
            @Parameter(name = "claimId", description = "Warranty claim ID", required = true, example = "2001")
    })
    public ResponseEntity<?> releasePart(@PathVariable("partNumber") String partNumber,
            @RequestParam("claimId") Long claimId) {
        try {
            boolean success = inventoryService.releaseReservedParts(claimId, partNumber);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Low Stock Parts", description = "Get parts at a location with quantity below a threshold.", parameters = {
            @Parameter(name = "locationId", description = "Inventory location ID", required = true, example = "100"),
            @Parameter(name = "threshold", description = "Threshold quantity", required = false, example = "10", schema = @Schema(defaultValue = "10"))
    })
    public ResponseEntity<?> getLowStockParts(@RequestParam("locationId") Long locationId,
            @RequestParam(value = "threshold", required = false, defaultValue = "10") Integer threshold) {
        try {
            List<Map<String, Object>> lowStockParts = inventoryService.getLowStockParts(locationId, threshold);
            return ResponseEntity.ok(lowStockParts);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/service-center/{serviceCenterId}")
    @Operation(summary = "Service Center Inventory Locations", description = "List inventory locations for a service center.", parameters = {
            @Parameter(name = "serviceCenterId", description = "Service center ID", required = true, example = "2") })
    public ResponseEntity<?> getServiceCenterInventory(@PathVariable("serviceCenterId") Long serviceCenterId) {
        try {
            List<Map<String, Object>> inventory = inventoryService.getInventoryLocationsByOrganization(serviceCenterId);
            return ResponseEntity.ok(inventory);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer Parts to Service Center", description = "Transfer parts from OEM inventory to a service center.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Transfer payload", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"partNumber\": \"BAT001\", \"quantity\": 5, \"oemInventoryId\": 10, \"serviceCenterId\": 2, \"transferReason\": \"Replenishment\"}"))))
    public ResponseEntity<?> transferParts(@RequestBody Map<String, Object> body) {
        try {
            String partNumber = String.valueOf(body.get("partNumber"));
            Integer quantity = Integer.valueOf(body.get("quantity").toString());
            Long oemInventoryId = Long.valueOf(body.get("oemInventoryId").toString());
            Long serviceCenterId = Long.valueOf(body.get("serviceCenterId").toString());
            String transferReason = String.valueOf(body.getOrDefault("transferReason", "Manual transfer"));

            boolean success = inventoryService.transferPartsToServiceCenter(oemInventoryId, serviceCenterId, partNumber,
                    quantity, transferReason);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
