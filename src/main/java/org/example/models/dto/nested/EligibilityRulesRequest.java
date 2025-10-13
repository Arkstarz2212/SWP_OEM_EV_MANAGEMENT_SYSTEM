package org.example.models.dto.nested;

import java.time.LocalDate;
import java.util.List;

/**
 * Nested DTO for recalls.eligibility_rules JSON field
 * Contains rules for recall targeting and eligibility
 */
public class EligibilityRulesRequest {
    // Date criteria
    private LocalDate purchaseDateStart;
    private LocalDate purchaseDateEnd;
    private LocalDate deliveryDateStart;
    private LocalDate deliveryDateEnd;

    // Vehicle criteria
    private List<String> eligibleModels;
    private List<String> eligibleVariants;
    private List<Integer> eligibleModelYears;
    private String batteryCapacityMin;
    private String batteryCapacityMax;

    // Customer criteria
    private List<String> eligibleCustomerTypes; // "individual", "corporate"
    private List<String> eligibleRegions;
    private List<String> excludedCustomerIds;

    // Usage criteria
    private Integer mileageMin;
    private Integer mileageMax;
    private boolean requireActiveWarranty = false;
    private boolean excludeExistingClaims = false;

    // Service center criteria
    private List<Long> eligibleServiceCenterIds;
    private List<String> eligibleServiceCenterRegions;

    // Advanced rules
    private String customCriteria; // JSON string for complex rules
    private boolean useAiTargeting = false;

    // Constructors
    public EligibilityRulesRequest() {
    }

    // Getters and Setters
    public LocalDate getPurchaseDateStart() {
        return purchaseDateStart;
    }

    public void setPurchaseDateStart(LocalDate purchaseDateStart) {
        this.purchaseDateStart = purchaseDateStart;
    }

    public LocalDate getPurchaseDateEnd() {
        return purchaseDateEnd;
    }

    public void setPurchaseDateEnd(LocalDate purchaseDateEnd) {
        this.purchaseDateEnd = purchaseDateEnd;
    }

    public LocalDate getDeliveryDateStart() {
        return deliveryDateStart;
    }

    public void setDeliveryDateStart(LocalDate deliveryDateStart) {
        this.deliveryDateStart = deliveryDateStart;
    }

    public LocalDate getDeliveryDateEnd() {
        return deliveryDateEnd;
    }

    public void setDeliveryDateEnd(LocalDate deliveryDateEnd) {
        this.deliveryDateEnd = deliveryDateEnd;
    }

    public List<String> getEligibleModels() {
        return eligibleModels;
    }

    public void setEligibleModels(List<String> eligibleModels) {
        this.eligibleModels = eligibleModels;
    }

    public List<String> getEligibleVariants() {
        return eligibleVariants;
    }

    public void setEligibleVariants(List<String> eligibleVariants) {
        this.eligibleVariants = eligibleVariants;
    }

    public List<Integer> getEligibleModelYears() {
        return eligibleModelYears;
    }

    public void setEligibleModelYears(List<Integer> eligibleModelYears) {
        this.eligibleModelYears = eligibleModelYears;
    }

    public String getBatteryCapacityMin() {
        return batteryCapacityMin;
    }

    public void setBatteryCapacityMin(String batteryCapacityMin) {
        this.batteryCapacityMin = batteryCapacityMin;
    }

    public String getBatteryCapacityMax() {
        return batteryCapacityMax;
    }

    public void setBatteryCapacityMax(String batteryCapacityMax) {
        this.batteryCapacityMax = batteryCapacityMax;
    }

    public List<String> getEligibleCustomerTypes() {
        return eligibleCustomerTypes;
    }

    public void setEligibleCustomerTypes(List<String> eligibleCustomerTypes) {
        this.eligibleCustomerTypes = eligibleCustomerTypes;
    }

    public List<String> getEligibleRegions() {
        return eligibleRegions;
    }

    public void setEligibleRegions(List<String> eligibleRegions) {
        this.eligibleRegions = eligibleRegions;
    }

    public List<String> getExcludedCustomerIds() {
        return excludedCustomerIds;
    }

    public void setExcludedCustomerIds(List<String> excludedCustomerIds) {
        this.excludedCustomerIds = excludedCustomerIds;
    }

    public Integer getMileageMin() {
        return mileageMin;
    }

    public void setMileageMin(Integer mileageMin) {
        this.mileageMin = mileageMin;
    }

    public Integer getMileageMax() {
        return mileageMax;
    }

    public void setMileageMax(Integer mileageMax) {
        this.mileageMax = mileageMax;
    }

    public boolean isRequireActiveWarranty() {
        return requireActiveWarranty;
    }

    public void setRequireActiveWarranty(boolean requireActiveWarranty) {
        this.requireActiveWarranty = requireActiveWarranty;
    }

    public boolean isExcludeExistingClaims() {
        return excludeExistingClaims;
    }

    public void setExcludeExistingClaims(boolean excludeExistingClaims) {
        this.excludeExistingClaims = excludeExistingClaims;
    }

    public List<Long> getEligibleServiceCenterIds() {
        return eligibleServiceCenterIds;
    }

    public void setEligibleServiceCenterIds(List<Long> eligibleServiceCenterIds) {
        this.eligibleServiceCenterIds = eligibleServiceCenterIds;
    }

    public List<String> getEligibleServiceCenterRegions() {
        return eligibleServiceCenterRegions;
    }

    public void setEligibleServiceCenterRegions(List<String> eligibleServiceCenterRegions) {
        this.eligibleServiceCenterRegions = eligibleServiceCenterRegions;
    }

    public String getCustomCriteria() {
        return customCriteria;
    }

    public void setCustomCriteria(String customCriteria) {
        this.customCriteria = customCriteria;
    }

    public boolean isUseAiTargeting() {
        return useAiTargeting;
    }

    public void setUseAiTargeting(boolean useAiTargeting) {
        this.useAiTargeting = useAiTargeting;
    }
}