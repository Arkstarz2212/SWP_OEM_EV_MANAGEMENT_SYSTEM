package org.example.models.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfileData {
    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address")
    private String address;

    @JsonProperty("mfa_enabled")
    private Boolean mfaEnabled;

    @JsonProperty("full_name")
    private String fullName;

    // Default constructor
    public ProfileData() {
    }

    // Constructor with all fields
    public ProfileData(String phone, String address, Boolean mfaEnabled, String fullName) {
        this.phone = phone;
        this.address = address;
        this.mfaEnabled = mfaEnabled;
        this.fullName = fullName;
    }

    // Getters and Setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(Boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}