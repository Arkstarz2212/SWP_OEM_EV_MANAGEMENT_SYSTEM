package org.example.models.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ServiceCenterCreateRequest {
    @NotBlank(message = "Code không được để trống")
    private String code;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    private String region;

    // Contact info fields
    @Email(message = "Email không hợp lệ")
    private String email;
    private String phone;
    private String address;
    private String contactPerson;
    private String license;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getLicense() { return license; }
    public void setLicense(String license) { this.license = license; }
}
