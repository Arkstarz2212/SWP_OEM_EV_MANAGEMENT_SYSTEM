package org.example.service.Serviceimpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.example.models.core.WarrantyPolicy;
import org.example.models.dto.request.WarrantyPolicyCreateRequest;
import org.example.models.dto.request.WarrantyPolicyUpdateRequest;
import org.example.models.dto.response.WarrantyPolicyResponse;
import org.example.repository.IRepository.IWarrantyPolicyRepository;
import org.example.service.IService.IWarrantyPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WarrantyPolicyService implements IWarrantyPolicyService {

    @Autowired
    private IWarrantyPolicyRepository warrantyPolicyRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public WarrantyPolicyResponse createPolicy(WarrantyPolicyCreateRequest request) {
        validateCreateRequest(request);

        // Check if policy code already exists
        if (warrantyPolicyRepository.existsByPolicyCode(request.getPolicyCode())) {
            throw new RuntimeException("Policy code already exists: " + request.getPolicyCode());
        }

        WarrantyPolicy policy = new WarrantyPolicy();
        policy.setOemId(request.getOemId());
        policy.setPolicyName(request.getPolicyName());
        policy.setPolicyCode(request.getPolicyCode());
        policy.setWarrantyPeriodMonths(request.getWarrantyMonths());
        policy.setWarrantyKmLimit(request.getWarrantyKm());
        policy.setIsActive(true); // Default to active
        policy.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);
        policy.setEffectiveFrom(request.getEffectiveFrom() != null
                ? request.getEffectiveFrom()
                : LocalDate.now());
        policy.setEffectiveTo(request.getEffectiveTo());

        // Build coverage details JSON
        String coverageDetails = buildCoverageDetailsJson(
                request.getBatteryCoverageMonths(),
                request.getBatteryCoverageKm(),
                request.getMotorCoverageMonths(),
                request.getMotorCoverageKm(),
                request.getInverterCoverageMonths(),
                request.getInverterCoverageKm());
        policy.setCoverageDetails(coverageDetails);

        WarrantyPolicy saved = warrantyPolicyRepository.save(policy);
        return toResponse(saved);
    }

    @Override
    public WarrantyPolicyResponse getPolicyById(Long policyId) {
        Optional<WarrantyPolicy> opt = warrantyPolicyRepository.findById(policyId);
        if (opt.isEmpty()) {
            throw new RuntimeException("Warranty policy not found");
        }
        return toResponse(opt.get());
    }

    @Override
    public WarrantyPolicyResponse updatePolicy(Long policyId, WarrantyPolicyUpdateRequest request) {
        Optional<WarrantyPolicy> opt = warrantyPolicyRepository.findById(policyId);
        if (opt.isEmpty()) {
            throw new RuntimeException("Warranty policy not found");
        }

        WarrantyPolicy policy = opt.get();

        if (request.getPolicyName() != null) {
            policy.setPolicyName(request.getPolicyName());
        }
        if (request.getPolicyCode() != null) {
            // Check if new policy code conflicts with existing one
            Optional<WarrantyPolicy> existing = warrantyPolicyRepository.findByPolicyCode(request.getPolicyCode());
            if (existing.isPresent() && !existing.get().getId().equals(policyId)) {
                throw new RuntimeException("Policy code already exists: " + request.getPolicyCode());
            }
            policy.setPolicyCode(request.getPolicyCode());
        }
        if (request.getWarrantyMonths() != null) {
            policy.setWarrantyPeriodMonths(request.getWarrantyMonths());
        }
        if (request.getWarrantyKm() != null) {
            policy.setWarrantyKmLimit(request.getWarrantyKm());
        }
        if (request.getIsActive() != null) {
            policy.setIsActive(request.getIsActive());
        }
        if (request.getIsDefault() != null) {
            policy.setIsDefault(request.getIsDefault());
        }
        if (request.getEffectiveFrom() != null) {
            policy.setEffectiveFrom(request.getEffectiveFrom());
        }
        if (request.getEffectiveTo() != null) {
            policy.setEffectiveTo(request.getEffectiveTo());
        }

        // Update coverage details if any coverage field is provided
        if (request.getBatteryCoverageMonths() != null || request.getBatteryCoverageKm() != null ||
                request.getMotorCoverageMonths() != null || request.getMotorCoverageKm() != null ||
                request.getInverterCoverageMonths() != null || request.getInverterCoverageKm() != null) {

            String currentCoverage = policy.getCoverageDetails();
            String updatedCoverage = updateCoverageDetails(currentCoverage,
                    request.getBatteryCoverageMonths(),
                    request.getBatteryCoverageKm(),
                    request.getMotorCoverageMonths(),
                    request.getMotorCoverageKm(),
                    request.getInverterCoverageMonths(),
                    request.getInverterCoverageKm());
            policy.setCoverageDetails(updatedCoverage);
        }

        WarrantyPolicy saved = warrantyPolicyRepository.save(policy);
        return toResponse(saved);
    }

    @Override
    public boolean deletePolicy(Long policyId) {
        if (!warrantyPolicyRepository.existsById(policyId)) {
            return false;
        }
        warrantyPolicyRepository.deleteById(policyId);
        return true;
    }

    @Override
    public List<WarrantyPolicyResponse> getAllPolicies(int limit, int offset) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findAll();
        return policies.stream()
                .skip(Math.max(0, offset))
                .limit(limit > 0 ? limit : policies.size())
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<WarrantyPolicyResponse> getPoliciesByOemId(Long oemId, int limit, int offset) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findByOemId(oemId);
        return policies.stream()
                .skip(Math.max(0, offset))
                .limit(limit > 0 ? limit : policies.size())
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<WarrantyPolicyResponse> getActivePolicies(int limit, int offset) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findActivePolicies();
        return policies.stream()
                .skip(Math.max(0, offset))
                .limit(limit > 0 ? limit : policies.size())
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<WarrantyPolicyResponse> searchPolicies(String keyword, int limit, int offset) {
        List<WarrantyPolicy> policies = warrantyPolicyRepository.findByPolicyNameContainingIgnoreCase(keyword);
        return policies.stream()
                .skip(Math.max(0, offset))
                .limit(limit > 0 ? limit : policies.size())
                .map(this::toResponse)
                .toList();
    }

    @Override
    public WarrantyPolicyResponse getPolicyByPolicyCode(String policyCode) {
        Optional<WarrantyPolicy> opt = warrantyPolicyRepository.findByPolicyCode(policyCode);
        if (opt.isEmpty()) {
            throw new RuntimeException("Warranty policy not found");
        }
        return toResponse(opt.get());
    }

    @Override
    public WarrantyPolicyResponse getDefaultPolicyByOemId(Long oemId) {
        Optional<WarrantyPolicy> opt = warrantyPolicyRepository.findByOemIdAndIsDefault(oemId, true);
        if (opt.isEmpty()) {
            throw new RuntimeException("Default warranty policy not found for OEM ID: " + oemId);
        }
        return toResponse(opt.get());
    }

    @Override
    public boolean activatePolicy(Long policyId) {
        Optional<WarrantyPolicy> opt = warrantyPolicyRepository.findById(policyId);
        if (opt.isEmpty()) {
            return false;
        }
        WarrantyPolicy policy = opt.get();
        policy.setIsActive(true);
        warrantyPolicyRepository.save(policy);
        return true;
    }

    @Override
    public boolean deactivatePolicy(Long policyId) {
        Optional<WarrantyPolicy> opt = warrantyPolicyRepository.findById(policyId);
        if (opt.isEmpty()) {
            return false;
        }
        WarrantyPolicy policy = opt.get();
        policy.setIsActive(false);
        warrantyPolicyRepository.save(policy);
        return true;
    }

    @Override
    public boolean setAsDefault(Long policyId, Long oemId) {
        Optional<WarrantyPolicy> opt = warrantyPolicyRepository.findById(policyId);
        if (opt.isEmpty()) {
            return false;
        }

        // Unset other default policies for this OEM
        List<WarrantyPolicy> existingDefaults = warrantyPolicyRepository.findByOemId(oemId);
        for (WarrantyPolicy existing : existingDefaults) {
            if (existing.getIsDefault() != null && existing.getIsDefault()) {
                existing.setIsDefault(false);
                warrantyPolicyRepository.save(existing);
            }
        }

        // Set this policy as default
        WarrantyPolicy policy = opt.get();
        policy.setIsDefault(true);
        warrantyPolicyRepository.save(policy);
        return true;
    }

    private void validateCreateRequest(WarrantyPolicyCreateRequest request) {
        if (request.getOemId() == null || request.getPolicyName() == null || request.getPolicyCode() == null
                || request.getWarrantyMonths() == null) {
            throw new IllegalArgumentException(
                    "Missing required fields: oemId, policyName, policyCode, warrantyMonths");
        }
    }

    private String buildCoverageDetailsJson(Integer batteryMonths, Integer batteryKm,
            Integer motorMonths, Integer motorKm,
            Integer inverterMonths, Integer inverterKm) {
        try {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"battery\":{\"months\":").append(batteryMonths != null ? batteryMonths : "null");
            json.append(",\"km\":").append(batteryKm != null ? batteryKm : "null").append("},");
            json.append("\"motor\":{\"months\":").append(motorMonths != null ? motorMonths : "null");
            json.append(",\"km\":").append(motorKm != null ? motorKm : "null").append("},");
            json.append("\"inverter\":{\"months\":").append(inverterMonths != null ? inverterMonths : "null");
            json.append(",\"km\":").append(inverterKm != null ? inverterKm : "null").append("}");
            json.append("}");
            return json.toString();
        } catch (Exception e) {
            return "{}";
        }
    }

    private String updateCoverageDetails(String currentCoverage,
            Integer batteryMonths, Integer batteryKm,
            Integer motorMonths, Integer motorKm,
            Integer inverterMonths, Integer inverterKm) {
        try {
            com.fasterxml.jackson.databind.JsonNode current = null;
            if (currentCoverage != null && !currentCoverage.isEmpty()) {
                current = objectMapper.readTree(currentCoverage);
            }

            if (current == null) {
                return buildCoverageDetailsJson(batteryMonths, batteryKm, motorMonths, motorKm, inverterMonths,
                        inverterKm);
            }

            // Update only provided fields
            com.fasterxml.jackson.databind.node.ObjectNode root = objectMapper.createObjectNode();

            com.fasterxml.jackson.databind.node.ObjectNode battery = objectMapper.createObjectNode();
            if (current.has("battery")) {
                battery.set("months", current.get("battery").get("months"));
                battery.set("km", current.get("battery").get("km"));
            }
            if (batteryMonths != null) {
                battery.put("months", batteryMonths);
            }
            if (batteryKm != null) {
                battery.put("km", batteryKm);
            }
            root.set("battery", battery);

            com.fasterxml.jackson.databind.node.ObjectNode motor = objectMapper.createObjectNode();
            if (current.has("motor")) {
                motor.set("months", current.get("motor").get("months"));
                motor.set("km", current.get("motor").get("km"));
            }
            if (motorMonths != null) {
                motor.put("months", motorMonths);
            }
            if (motorKm != null) {
                motor.put("km", motorKm);
            }
            root.set("motor", motor);

            com.fasterxml.jackson.databind.node.ObjectNode inverter = objectMapper.createObjectNode();
            if (current.has("inverter")) {
                inverter.set("months", current.get("inverter").get("months"));
                inverter.set("km", current.get("inverter").get("km"));
            }
            if (inverterMonths != null) {
                inverter.put("months", inverterMonths);
            }
            if (inverterKm != null) {
                inverter.put("km", inverterKm);
            }
            root.set("inverter", inverter);

            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            return currentCoverage != null ? currentCoverage : "{}";
        }
    }

    private WarrantyPolicyResponse toResponse(WarrantyPolicy policy) {
        WarrantyPolicyResponse response = new WarrantyPolicyResponse();
        response.setId(policy.getId());
        response.setOemId(policy.getOemId());
        response.setPolicyCode(policy.getPolicyCode());
        response.setPolicyName(policy.getPolicyName());
        response.setDescription(policy.getDescription());
        response.setEffectiveFrom(policy.getEffectiveFrom());
        response.setEffectiveTo(policy.getEffectiveTo());
        response.setWarrantyPeriodMonths(policy.getWarrantyPeriodMonths());
        response.setWarrantyKmLimit(policy.getWarrantyKmLimit());
        response.setCoverageDetails(policy.getCoverageDetails());
        response.setIsActive(policy.getIsActive());
        response.setIsDefault(policy.getIsDefault());
        response.setCreatedAt(policy.getCreatedAt());
        response.setCreatedByUserId(policy.getCreatedByUserId());
        response.setUpdatedAt(policy.getUpdatedAt());
        response.setUpdatedByUserId(policy.getUpdatedByUserId());

        // Legacy fields
        response.setModel(policy.getModel());
        response.setComponentCategory(policy.getComponentCategory());
        response.setMonthsCoverage(policy.getMonthsCoverage());
        response.setKmCoverage(policy.getKmCoverage());
        response.setNotes(policy.getNotes());

        return response;
    }
}
