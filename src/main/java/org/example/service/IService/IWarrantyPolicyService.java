package org.example.service.IService;

import java.util.List;

import org.example.models.dto.request.WarrantyPolicyCreateRequest;
import org.example.models.dto.request.WarrantyPolicyUpdateRequest;
import org.example.models.dto.response.WarrantyPolicyResponse;

public interface IWarrantyPolicyService {
    // CRUD Operations
    WarrantyPolicyResponse createPolicy(WarrantyPolicyCreateRequest request);

    WarrantyPolicyResponse getPolicyById(Long policyId);

    WarrantyPolicyResponse updatePolicy(Long policyId, WarrantyPolicyUpdateRequest request);

    boolean deletePolicy(Long policyId);

    // List and Search
    List<WarrantyPolicyResponse> getAllPolicies(int limit, int offset);

    List<WarrantyPolicyResponse> getPoliciesByOemId(Long oemId, int limit, int offset);

    List<WarrantyPolicyResponse> getActivePolicies(int limit, int offset);

    List<WarrantyPolicyResponse> searchPolicies(String keyword, int limit, int offset);

    WarrantyPolicyResponse getPolicyByPolicyCode(String policyCode);

    WarrantyPolicyResponse getDefaultPolicyByOemId(Long oemId);

    // Status Operations
    boolean activatePolicy(Long policyId);

    boolean deactivatePolicy(Long policyId);

    boolean setAsDefault(Long policyId, Long oemId);
}
