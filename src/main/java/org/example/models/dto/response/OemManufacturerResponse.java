package org.example.models.dto.response;

public class OemManufacturerResponse {
    private Long id;
    private String code;
    private String name;
    private String contact;

    // Constructors
    public OemManufacturerResponse() {
    }

    public OemManufacturerResponse(Long id, String code, String name, String contact) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.contact = contact;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "OemManufacturerResponse{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}