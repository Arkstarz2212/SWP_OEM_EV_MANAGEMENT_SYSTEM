package org.example.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.example.models.core.Role;
import org.example.models.dto.request.RoleCreateRequest;
import org.example.models.dto.request.RoleUpdateRequest;
import org.example.models.dto.response.RoleResponse;
import org.example.service.IService.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "Role and permission management for user access control")
public class RolesController {

    @Autowired
    private IRoleService roleService;

    @GetMapping
    @Operation(summary = "List All Roles", description = "Retrieve a list of all available roles in the system with their details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully", content = @Content(schema = @Schema(implementation = RoleResponse.class)))
    })
    public List<RoleResponse> list() {
        return roleService.listAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Role by ID", description = "Retrieve a specific role by its unique identifier.", parameters = {
            @Parameter(name = "id", description = "Role ID", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<RoleResponse> get(@PathVariable Long id) {
        Role r = roleService.getById(id);
        return r == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(toResponse(r));
    }

    @GetMapping("/by-code/{code}")
    @Operation(summary = "Get Role by Code", description = "Retrieve a specific role by its unique code identifier.", parameters = {
            @Parameter(name = "code", description = "Role code", required = true, example = "ADMIN")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<RoleResponse> getByCode(@PathVariable String code) {
        Role r = roleService.getByCode(code);
        return r == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(toResponse(r));
    }

    @PostMapping
    @Operation(summary = "Create New Role", description = "Create a new role with specified code, name, and description.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Role creation data", required = true, content = @Content(schema = @Schema(implementation = RoleCreateRequest.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role created successfully", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<RoleResponse> create(@Validated @RequestBody RoleCreateRequest req) {
        Role role = new Role(req.getCode(), req.getName(), req.getDescription());
        Role saved = roleService.create(role);
        return ResponseEntity.ok(toResponse(saved));
    }

    @PutMapping
    @Operation(summary = "Update Role", description = "Update an existing role's name and description.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Role update data", required = true, content = @Content(schema = @Schema(implementation = RoleUpdateRequest.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<RoleResponse> update(@Validated @RequestBody RoleUpdateRequest req) {
        Role existing = roleService.getById(req.getId());
        if (existing == null)
            return ResponseEntity.notFound().build();
        existing.setName(req.getName());
        existing.setDescription(req.getDescription());
        Role updated = roleService.update(existing);
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Role", description = "Delete a role by its ID. This action cannot be undone.", parameters = {
            @Parameter(name = "id", description = "Role ID to delete", required = true, example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private RoleResponse toResponse(Role r) {
        return new RoleResponse(r.getId(), r.getCode(), r.getName(), r.getDescription());
    }
}
